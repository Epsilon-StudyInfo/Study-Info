package com.studyinfo.app.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.studyinfo.app.ServiceLocator
import java.util.concurrent.TimeUnit

/**
 * Periodically pushes pending local changes to Firestore and pulls remote changes.
 *
 * Cloud sync only runs when a FIREBASE-backed user is signed in. Local-only accounts
 * (the default when no Firebase project is configured) never hit the network — the app
 * is fully offline-first.
 *
 * Triggers:
 *   - Periodic schedule (every 15 minutes when network is available)
 *   - Manual via [enqueueOneTime] (wired to Settings -> "Sync now")
 */
class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val sl = ServiceLocator
        // The ServiceLocator's properties are lateinit; if init() wasn't called yet (which
        // should not happen because the App class calls it in onCreate), bail out gracefully.
        if (!sl.isInitialised()) return Result.success()
        // Only run cloud sync for Firebase-backed sessions; local-only accounts are offline.
        if (sl.authRepository.firebaseUid == null) return Result.success()

        var hasFailure = false

        try { sl.tagRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.tagRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.customSourceRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.customSourceRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.chapterRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.chapterRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.errorRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.errorRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.unsolvedRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.unsolvedRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.taskRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.taskRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.progressRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.progressRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.reviewRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.reviewRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.questionImageRepository.pushPending() } catch (_: Exception) { hasFailure = true }
        try { sl.questionImageRepository.pullAll() } catch (_: Exception) { hasFailure = true }
        try { sl.taskRepository.pushStreakPending() } catch (_: Exception) { hasFailure = true }
        try { sl.taskRepository.pullStreakAll() } catch (_: Exception) { hasFailure = true }

        // Process image upload queue (needs Storage, not just Firestore)
        try {
            val queue = sl.database().syncQueueDao().pending(limit = 20)
            for (item in queue) {
                sl.questionImageRepository.processQueuedUpload(item)
            }
        } catch (_: Exception) {
            hasFailure = true
        }

        return if (hasFailure) Result.retry() else Result.success()
    }

    companion object {
        const val PERIODIC_TAG = "prepvault-sync"
        const val ONE_TIME_TAG = "prepvault-sync-onetime"

        fun schedulePeriodic(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<SyncWorker>(
                15, TimeUnit.MINUTES,
            ).setConstraints(constraints).addTag(PERIODIC_TAG).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PERIODIC_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
        }

        fun enqueueOneTime(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .addTag(ONE_TIME_TAG)
                .build()

            // REPLACE so repeated taps don't stack duplicate sync jobs.
            WorkManager.getInstance(context).enqueueUniqueWork(
                ONE_TIME_TAG,
                ExistingWorkPolicy.REPLACE,
                request,
            )
        }

        fun cancelAll(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(PERIODIC_TAG)
            WorkManager.getInstance(context).cancelAllWorkByTag(ONE_TIME_TAG)
        }
    }
}
