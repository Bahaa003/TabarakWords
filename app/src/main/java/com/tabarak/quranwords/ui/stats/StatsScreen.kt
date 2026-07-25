package com.tabarak.quranwords.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tabarak.quranwords.ui.components.AppBottomBar
import com.tabarak.quranwords.ui.theme.ErrorRed
import com.tabarak.quranwords.ui.theme.SuccessGreen
import com.tabarak.quranwords.util.viewModelWithRepo

@Composable
fun StatsScreen(navController: NavHostController) {
    val viewModel = viewModelWithRepo { repo -> StatsViewModel(repo) }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("الإحصائيات") }) },
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(3.dp)) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("نسبة الإنجاز", style = MaterialTheme.typography.titleMedium)
                    LinearProgressIndicator(
                        progress = state.progressPercent / 100f,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    )
                    Text(
                        "${state.progressPercent}%",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            StatRow(label = "عدد الكلمات الكلي", value = state.total)
            StatRow(label = "عدد الكلمات التي راجعتها", value = state.reviewed)
            StatRow(label = "عدد الكلمات التي أعرفها", value = state.known, color = SuccessGreen)
            StatRow(label = "عدد الكلمات التي لا أعرفها", value = state.unknown, color = ErrorRed)
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(
                value.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = color,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
