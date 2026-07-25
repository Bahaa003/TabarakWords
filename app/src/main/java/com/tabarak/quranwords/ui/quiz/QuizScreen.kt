package com.tabarak.quranwords.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tabarak.quranwords.ui.theme.ErrorRed
import com.tabarak.quranwords.ui.theme.SuccessGreen
import com.tabarak.quranwords.util.viewModelWithRepo

@Composable
fun QuizScreen(navController: NavHostController, source: String) {
    val viewModel = viewModelWithRepo { repo -> QuizViewModel(repo, source) }
    val words by viewModel.words.collectAsState()
    val index by viewModel.index.collectAsState()
    val revealed by viewModel.revealed.collectAsState()
    val known by viewModel.knownCount.collectAsState()
    val unknown by viewModel.unknownCount.collectAsState()
    val finished by viewModel.finished.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("اختبار الحفظ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { padding ->
        if (words.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { Text("لا توجد كلمات لعرضها") }
            return@Scaffold
        }

        if (finished) {
            QuizResult(known = known, unknown = unknown, total = words.size, onRestart = { viewModel.restart() })
            return@Scaffold
        }

        val word = words[index]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = (index + 1) / words.size.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${index + 1} / ${words.size}      أعرفها: $known   لا أعرفها: $unknown",
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            word.word,
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (revealed) {
                            Text(
                                word.meaning,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
            }

            if (!revealed) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.answer(true) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null)
                        Text("  أعرفها")
                    }
                    Button(
                        onClick = { viewModel.answer(false) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = null)
                        Text("  لا أعرفها")
                    }
                }
            } else {
                Button(
                    onClick = { viewModel.next() },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("التالي")
                }
            }
        }
    }
}

@Composable
private fun QuizResult(known: Int, unknown: Int, total: Int, onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("انتهى الاختبار!", style = MaterialTheme.typography.headlineMedium)
            Text(
                "أعرفها: $known  من أصل $total",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = SuccessGreen
            )
            Text(
                "لا أعرفها: $unknown",
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = ErrorRed
            )
            OutlinedButton(onClick = onRestart, modifier = Modifier.padding(top = 24.dp)) {
                Text("إعادة الاختبار")
            }
        }
    }
}
