package com.tabarak.quranwords.ui.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tabarak.quranwords.QuranWordsApp
import com.tabarak.quranwords.ui.components.AppBottomBar
import com.tabarak.quranwords.ui.navigation.Routes
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val app = context.applicationContext as QuranWordsApp
    val scope = rememberCoroutineScope()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var selectedJuz by rememberSaveable { mutableStateOf("جزء تبارك") }
    var wordInput by rememberSaveable { mutableStateOf("") }
    var meaningInput by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("كلمات تبارك وعمّ") })
        },
        bottomBar = { AppBottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "إضافة كلمة")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "اختر الجزء الذي تريد مراجعة كلماته الغريبة:",
                style = MaterialTheme.typography.bodyLarge
            )

            JuzCard(title = "جزء عمّ", subtitle = "من سورة النبأ إلى سورة الناس") {
                navController.navigate(Routes.surahs("جزء عم"))
            }

            JuzCard(title = "جزء تبارك", subtitle = "من سورة الملك إلى سورة المرسلات") {
                navController.navigate(Routes.surahs("جزء تبارك"))
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("إضافة كلمة جديدة") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("اختر الجزء:")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = selectedJuz == "جزء عم",
                            onClick = { selectedJuz = "جزء عم" },
                            label = { Text("جزء عم") }
                        )
                        FilterChip(
                            selected = selectedJuz == "جزء تبارك",
                            onClick = { selectedJuz = "جزء تبارك" },
                            label = { Text("جزء تبارك") }
                        )
                    }

                    OutlinedTextField(
                        value = wordInput,
                        onValueChange = { wordInput = it },
                        label = { Text("الكلمة الأساسية") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = meaningInput,
                        onValueChange = { meaningInput = it },
                        label = { Text("معنى الكلمة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val cleanedWord = wordInput.trim()
                    val cleanedMeaning = meaningInput.trim()
                    if (cleanedWord.isNotEmpty() && cleanedMeaning.isNotEmpty()) {
                        scope.launch {
                            app.repository.addCustomWord(selectedJuz, cleanedWord, cleanedMeaning)
                        }
                        Toast.makeText(context, "تمت إضافة الكلمة بنجاح", Toast.LENGTH_SHORT).show()
                        showAddDialog = false
                        wordInput = ""
                        meaningInput = ""
                    }
                }) {
                    Text("إضافة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
private fun JuzCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.AutoStories,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(start = 12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Filled.ChevronLeft, contentDescription = null)
        }
    }
}
