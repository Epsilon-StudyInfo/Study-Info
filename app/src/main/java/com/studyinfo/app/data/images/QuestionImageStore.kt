package com.studyinfo.app.data.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Private, durable on-device storage for question images.
 *
 * Why copy at all? The photo picker hands out `content://` uris with a TRANSIENT read
 * grant: after the process dies the grant is gone and the uri can no longer be opened,
 * which would break offline display AND the later Firebase Storage upload. Every picked
 * image is therefore imported once into app-private storage (`filesDir/question_images`)
 * — no permissions needed, never visible to other apps — and the stable `file://` path is
 * what Room and the upload queue keep.
 *
 * Images are downscaled to at most [MAX_DIMENSION] px and re-encoded as JPEG under the
 * [MAX_BYTES] ceiling so they always satisfy the 5 MB per-file Storage security rule.
 */
class QuestionImageStore(context: Context) {

    private val appContext = context.applicationContext

    fun imagesDir(): File = File(appContext.filesDir, DIR).apply { mkdirs() }

    /**
     * Imports a picked image into private storage. Returns the stable `file://` uri, or
     * null when the source cannot be decoded (caller shows a pick-failed error).
     */
    fun importToPrivateStorage(source: Uri): String? {
        return try {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            appContext.contentResolver.openInputStream(source)?.use {
                BitmapFactory.decodeStream(it, null, bounds)
            }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

            // Decode at reduced size straight away (power-of-two sample close to target).
            val sample = sampleSizeFor(bounds.outWidth, bounds.outHeight)
            val options = BitmapFactory.Options().apply { inSampleSize = sample }
            val decoded = appContext.contentResolver.openInputStream(source)?.use {
                BitmapFactory.decodeStream(it, null, options)
            } ?: return null

            // Exact downscale if the sample still leaves the long edge above the cap.
            val bitmap = scaleDown(decoded)

            // JPEG has no alpha: flatten any transparency onto white.
            val flat = if (bitmap.hasAlpha()) flattenToWhite(bitmap) else bitmap

            val target = File(imagesDir(), "${UUID.randomUUID()}.jpg")
            compressUnderLimit(flat, target)

            if (flat !== bitmap) bitmap.recycle()
            Uri.fromFile(target).toString()
        } catch (e: Exception) {
            Log.w(TAG, "importToPrivateStorage: failed to import picked image", e)
            null
        }
    }

    /** Removes the private file behind a stored local uri (best-effort, never throws). */
    fun deleteByUri(uriString: String?) {
        if (uriString.isNullOrBlank()) return
        runCatching {
            val uri = Uri.parse(uriString)
            val path = uri.path ?: return
            val file = File(path)
            // Only ever delete inside our own private dir — defensive against bad rows.
            if (file.absolutePath.startsWith(imagesDir().absolutePath)) {
                file.delete()
            }
        }.onFailure { Log.w(TAG, "deleteByUri: failed", it) }
    }

    // ---------------------------------------------------------------- helpers

    private fun sampleSizeFor(width: Int, height: Int): Int {
        var sample = 1
        var longEdge = max(width, height)
        while (longEdge / 2 >= MAX_DIMENSION) {
            sample *= 2
            longEdge /= 2
        }
        return sample
    }

    private fun scaleDown(bitmap: Bitmap): Bitmap {
        val longEdge = max(bitmap.width, bitmap.height)
        if (longEdge <= MAX_DIMENSION) return bitmap
        val scale = MAX_DIMENSION.toFloat() / longEdge
        val w = (bitmap.width * scale).roundToInt().coerceAtLeast(1)
        val h = (bitmap.height * scale).roundToInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, w, h, true)
    }

    private fun flattenToWhite(source: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(source, 0f, 0f, null)
        if (result !== source) source.recycle()
        return result
    }

    /**
     * Writes the JPEG, stepping the quality down until the file is under [MAX_BYTES]
     * (the Storage rule rejects anything at or above 5 MB). The 0.6 ladder keeps text in
     * question screenshots readable while converging quickly.
     */
    private fun compressUnderLimit(bitmap: Bitmap, target: File) {
        var quality = INITIAL_QUALITY
        while (true) {
            FileOutputStream(target).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            }
            if (target.length() <= MAX_BYTES || quality <= MIN_QUALITY) return
            quality = (quality * 0.6f).roundToInt().coerceAtLeast(MIN_QUALITY)
        }
    }

    private companion object {
        const val TAG = "QuestionImageStore"
        const val DIR = "question_images"
        const val MAX_DIMENSION = 1920
        const val INITIAL_QUALITY = 85
        const val MIN_QUALITY = 25

        /** Hard ceiling under the Storage rule's 5 MB per-image limit. */
        const val MAX_BYTES = 4L * 1024 * 1024
    }
}
