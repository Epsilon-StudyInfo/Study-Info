package com.studyinfo.app

import android.app.Application
import androidx.work.Configuration
import com.studyinfo.app.sync.SyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Application class. Sets up:
 *   - Manual DI container ([ServiceLocator]) — including safe, optional Firebase init
 *   - Default JEE chapter seed (only on first launch)
 *   - WorkManager periodic sync (cloud sync only active for Firebase-backed accounts)
 */
class PrepVaultApp : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Firebase is optional: with a placeholder google-services.json (or none at all)
        // the app still runs fully in local-only mode. Never crash on Firebase setup.
        runCatching {
            com.google.firebase.FirebaseApp.initializeApp(this)
            runCatching {
                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance()
                    .isCrashlyticsCollectionEnabled = true
            }
        }

        ServiceLocator.init(this)

        appScope.launch {
            try { ServiceLocator.seedIfEmpty(this@PrepVaultApp) } catch (_: Throwable) {}
        }

        runCatching { SyncWorker.schedulePeriodic(this) }
    }
}
