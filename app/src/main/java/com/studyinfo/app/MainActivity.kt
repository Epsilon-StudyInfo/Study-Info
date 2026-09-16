package com.studyinfo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.studyinfo.app.sync.SyncWorker
import com.studyinfo.app.ui.PrepVaultRoot
import com.studyinfo.app.ui.theme.PrepVaultTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.Transparent, Color.Transparent),
            navigationBarStyle = SystemBarStyle.auto(Color.Transparent, Color.Transparent),
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

        // Kick off a one-time sync after the UI is up.
        lifecycleScope.launch {
            SyncWorker.enqueueOneTime(this@MainActivity)
        }
    }
}
