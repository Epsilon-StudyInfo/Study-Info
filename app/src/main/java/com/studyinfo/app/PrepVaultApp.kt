package com.studyinfo.app

import android.app.Application
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.studyinfo.app.sync.SyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Application class. Sets up:
 *   - Manual DI container ([ServiceLocator])
 *   - Default JEE chapter seed (only on first launch)
 *   - WorkManager periodic sync
 *   - Crashlytics opt-in (no PII collected — see privacy section in README)
 */
class PrepVaultApp : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // Crashlytics: collect crashes only (no question content / notes / solutions).
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

        ServiceLocator.init(this)

        appScope.launch {
            try { ServiceLocator.seedIfEmpty(this@PrepVaultApp) } catch (_: Throwable) {}
        }

        SyncWorker.schedulePeriodic(this)
    }
}
