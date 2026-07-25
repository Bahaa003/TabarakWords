package com.tabarak.quranwords.ui.flashcard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tabarak.quranwords.util.viewModelWithRepo

@Composable
fun FlashcardScreen(navController: NavHostController, source: String) {
    val viewModel = viewModelWithRepo { repo -> FlashcardViewModel(repo, source) }
    val words by viewModel.words.collectAsState()
    val index by viewModel.index.collectAsState()
    val showMeaning by viewModel.showMeaning.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("بطاقات الحفظ") },
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("لا توجد كلمات لعرضها", style = MaterialTheme.typography.bodyLarge)
            }
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
                "${index + 1} / ${words.size}",
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val rotation by animateFloatAsState(
                    targetValue = if (showMeaning) 180f else 0f,
                    animationSpec = tween(350),
                    label = "flip"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clickable { viewModel.flip() },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (rotation <= 90f) {
                            Text(
                                word.word,
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            Text(
                                word.meaning,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .graphicsLayer { rotationY = 180f }
                            )
                        }
                    }
                }
            }

            Row_Buttons(
                onFavorite = { viewModel.toggleFavoriteCurrent() },
                isFavorite = word.isFavorite,
                onFlip = { viewModel.flip() },
                showMeaning = showMeaning,
                onPrevious = { viewModel.previous() },
                onNext = { viewModel.next() },
                hasPrevious = index > 0,
                hasNext = index < words.lastIndex
            )
        }
    }
}

@Composable
private fun Row_Buttons(
    onFavorite: () -> Unit,
    isFavorite: Boolean,
    onFlip: () -> Unit,
    showMeaning: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    hasPrevious: Boolean,
    hasNext: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onFlip) {
                Text(if (showMeaning) "إخفاء المعنى" else "إظهار المعنى")
            }
            IconButton(onClick = onFavorite) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "مفضلة"
                )
            }
        }

        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onPrevious, enabled = hasPrevious) {
                Text("السابق")
            }
            OutlinedButton(onClick = onNext, enabled = hasNext) {
                Text("التالي")
            }
        }
    }
}
