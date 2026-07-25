package com.tabarak.quranwords.ui.random

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tabarak.quranwords.ui.components.AppBottomBar
import com.tabarak.quranwords.ui.navigation.Routes
import com.tabarak.quranwords.ui.navigation.SOURCE_RANDOM

@Composable
fun RandomReviewScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("المراجعة العشوائية") }) },
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.Shuffle,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "راجع 30 كلمة عشوائية من جميع سور جزأي عمّ وتبارك",
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = { navController.navigate(Routes.flashcard(SOURCE_RANDOM)) }) {
                    Text("بطاقات عشوائية")
                }
                OutlinedButton(
                    onClick = { navController.navigate(Routes.quiz(SOURCE_RANDOM)) },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text("اختبار عشوائي")
                }
            }
        }
    }
}
