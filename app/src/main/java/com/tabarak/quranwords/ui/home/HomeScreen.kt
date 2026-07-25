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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.tabarak.quranwords.util.rememberRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val app = context.applicationContext as QuranWordsApp
    val repo = rememberRepository()
    val scope = rememberCoroutineScope()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var currentStep by rememberSaveable { mutableStateOf(1) }
    var selectedJuz by rememberSaveable { mutableStateOf("جزء تبارك") }
    var selectedSurah by rememberSaveable { mutableStateOf("سورة الملك") }
    var wordInput by rememberSaveable { mutableStateOf("") }
    var meaningInput by rememberSaveable { mutableStateOf("") }
    var surahOptions by remember { mutableStateOf(listOf("سورة الملك")) }

    LaunchedEffect(selectedJuz) {
        val list = repo.getSurahsForJuz(selectedJuz)
        if (list.isNotEmpty()) {
            surahOptions = list
            if (selectedSurah !in list) {
                selectedSurah = list.first()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("كلمات تبارك وعمّ") })
        },
        bottomBar = { AppBottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                currentStep = 1
                showAddDialog = true
            }) {
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
            onDismissRequest = {
                showAddDialog = false
                currentStep = 1
            },
            title = {
                Text(
                    when (currentStep) {
                        1 -> "اختر الجزء"
                        2 -> "اختر السورة"
                        else -> "أدخل الكلمة والمعنى"
                    }
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    when (currentStep) {
                        1 -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = selectedJuz == "جزء عم",
                                    onClick = {
                                        selectedJuz = "جزء عم"
                                    },
                                    label = { Text("جزء عم") }
                                )
                                FilterChip(
                                    selected = selectedJuz == "جزء تبارك",
                                    onClick = {
                                        selectedJuz = "جزء تبارك"
                                    },
                                    label = { Text("جزء تبارك") }
                                )
                            }
                        }
                        2 -> {
                            val scrollState = rememberScrollState()
                            Column(
                                modifier = Modifier.verticalScroll(scrollState),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                surahOptions.forEach { surah ->
                                    FilterChip(
                                        selected = selectedSurah == surah,
                                        onClick = { selectedSurah = surah.trim() },
                                        label = { Text(surah) }
                                    )
                                }
                            }
                        }
                        else -> {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    when (currentStep) {
                        1 -> currentStep = 2
                        2 -> currentStep = 3
                        else -> {
                            val cleanedWord = wordInput.trim()
                            val cleanedMeaning = meaningInput.trim()
                            val chosenJuz = selectedJuz.trim()
                            val chosenSurah = selectedSurah.trim().ifEmpty {
                                surahOptions.firstOrNull() ?: if (chosenJuz == "جزء عم") "سورة النبأ" else "سورة الملك"
                            }

                            if (cleanedWord.isNotEmpty() && cleanedMeaning.isNotEmpty()) {
                                selectedSurah = chosenSurah
                                scope.launch {
                                    app.repository.addCustomWord(chosenJuz, chosenSurah, cleanedWord, cleanedMeaning)
                                }
                                Toast.makeText(context, "تمت إضافة الكلمة بنجاح", Toast.LENGTH_SHORT).show()
                                showAddDialog = false
                                currentStep = 1
                                wordInput = ""
                                meaningInput = ""
                                selectedSurah = chosenSurah
                            }
                        }
                    }
                }) {
                    Text(
                        when (currentStep) {
                            1 -> "التالي"
                            2 -> "التالي"
                            else -> "إضافة"
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    if (currentStep == 1) {
                        showAddDialog = false
                        currentStep = 1
                    } else {
                        currentStep -= 1
                    }
                }) {
                    Text(if (currentStep == 1) "إلغاء" else "رجوع")
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
