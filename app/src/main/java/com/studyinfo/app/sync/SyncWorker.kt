package com.studyinfo.app.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.studyinfo.app.ServiceLocator
import java.util.concurrent.TimeUnit

/**
 * Periodically pushes pending local changes to Firestore and pulls remote changes.
 *
 * Triggers:
 *   - Periodic schedule (every 15 minutes when network is available)
 *   - Manual via [enqueueOneTime]
 *
 * The worker reads every repository's `pendingChanges()` list, writes each item to
 * Firestore, marks the row SYNCED on success, and on failure leaves the row pending
 * (the next run will retry).
 */
class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val sl = ServiceLocator
        if (!sl::authRepository.isInitialized) return Result.success()
        if (sl.authRepository.currentUid == null) return Result.success()

        var totalPushed = 0
        var totalPulled = 0
        var hasFailure = false

        suspend fun <T> runStep(name: String, push: suspend () -> Int, pull: suspend () -> Int) {
            try {
                totalPushed += push()
                totalPulled += pull()
            } catch (e: Exception) {
                hasFailure = true
            }
        }

        runStep("tags", sl.tagRepository::pushPending, sl.tagRepository::pullAll)
        runStep("customSources", sl.customSourceRepository::pushPending, sl.customSourceRepository::pullAll)
        runStep("chapters", sl.chapterRepository::pushPending, sl.chapterRepository::pullAll)
        runStep("errors", sl.errorRepository::pushPending, sl.errorRepository::pullAll)
        runStep("unsolved", sl.unsolvedRepository::pushPending, sl.unsolvedRepository::pullAll)
        runStep("tasks", sl.taskRepository::pushPending, sl.taskRepository::pullAll)
        runStep("progress", sl.progressRepository::pushPending, sl.progressRepository::pullAll)
        runStep("reviews", sl.reviewRepository::pushPending, sl.reviewRepository::pullAll)
        runStep("questionImages", sl.questionImageRepository::pushPending, sl.questionImageRepository::pullAll)
        runStep("streak", sl.taskRepository::pushStreakPending, sl.taskRepository::pullStreakAll)

        // Process image upload queue (needs Storage, not just Firestore)
        try {
            val queue = sl.db().syncQueueDao().pending(limit = 20)
            for (item in queue) {
                sl.questionImageRepository.processQueuedUpload(item)
            }
        } catch (e: Exception) {
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

            val request = androidx.work.OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .addTag(ONE_TIME_TAG)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }

        fun cancelAll(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(PERIODIC_TAG)
            WorkManager.getInstance(context).cancelAllWorkByTag(ONE_TIME_TAG)
        }
    }
}
