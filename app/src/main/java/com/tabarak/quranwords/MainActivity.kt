package com.tabarak.quranwords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import com.tabarak.quranwords.ui.navigation.AppNavGraph
import com.tabarak.quranwords.ui.theme.TabarakWordsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as QuranWordsApp

        setContent {
            // تعبئة قاعدة البيانات لأول مرة من ملف JSON
            LaunchedEffect(Unit) {
                launch { app.repository.ensureSeeded() }
            }

            TabarakWordsTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavGraph()
                    }
                }
            }
        }
    }
}
