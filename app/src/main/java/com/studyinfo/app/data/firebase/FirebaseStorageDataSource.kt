package com.studyinfo.app.data.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File

/**
 * Wrapper around Firebase Storage for question-image upload/delete.
 *
 * Path layout: `users/{uid}/questions/{questionId}/{imageId}`.
 * Storage rules enforce `request.auth.uid == uid`.
 */
class FirebaseStorageDataSource(
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val uidProvider: () -> String?,
) {

    private fun refFor(uid: String, questionId: String, imageId: String): StorageReference =
        storage.reference
            .child("users")
            .child(uid)
            .child("questions")
            .child(questionId)
            .child(imageId)

    /** Canonical storage path for an image, e.g. `users/{uid}/questions/{qId}/{imgId}`. */
    fun pathFor(questionId: String, imageId: String): String? {
        val uid = uidProvider() ?: return null
        return "users/$uid/questions/$questionId/$imageId"
    }

    suspend fun uploadFromUri(
        questionId: String,
        imageId: String,
        sourceUri: Uri,
        compressedFile: File? = null,
    ): Result<String> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("Not signed in"))
        return runCatching {
            val ref = refFor(uid, questionId, imageId)
            val task = if (compressedFile != null) ref.putFile(Uri.fromFile(compressedFile)) else ref.putFile(sourceUri)
            val snapshot = task.await()
            snapshot.storage.downloadUrl.await().toString()
        }
    }

    suspend fun deleteByPath(path: String): Result<Unit> = runCatching {
        storage.reference.child(path).delete().await()
    }

    suspend fun deleteByRemoteUrl(url: String): Result<Unit> = runCatching {
        storage.getReferenceFromUrl(url).delete().await()
    }

    /**
     * Best-effort recursive delete of the user's entire storage tree at account deletion.
     * Storage doesn't have a native "delete folder" call; we list and delete in batches.
     */
    suspend fun deleteAllForUser(): Result<Unit> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("Not signed in"))
        return runCatching {
            val userQuestionsRef = storage.reference.child("users").child(uid).child("questions")
            deleteRecursively(userQuestionsRef)
        }
    }

    private suspend fun deleteRecursively(ref: StorageReference) {
        val items = ref.listAll().await()
        for (item in items.items) item.delete().await()
        for (prefix in items.prefixes) deleteRecursively(prefix)
    }
}
