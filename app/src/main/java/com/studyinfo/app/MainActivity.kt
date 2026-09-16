package com.studyinfo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.studyinfo.app.sync.SyncWorker
import com.studyinfo.app.ui.PrepVaultRoot
import com.studyinfo.app.ui.theme.PrepVaultTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transparent = android.graphics.Color.TRANSPARENT
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(transparent, transparent),
            navigationBarStyle = SystemBarStyle.auto(transparent, transparent),
        )
        setContent {
            PrepVaultTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    PrepVaultRoot()
                }
            }
        }

        // Kick off a one-time sync after the UI is up. enqueueUniqueWork(REPLACE) means
        // rotations / re-creations never stack duplicate jobs.
        runCatching { SyncWorker.enqueueOneTime(this) }
    }
}
