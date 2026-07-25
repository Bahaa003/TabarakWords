package com.tabarak.quranwords.ui.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarak.quranwords.data.WordEntity
import com.tabarak.quranwords.data.WordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class WordListViewModel(private val repository: WordRepository, private val surah: String) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    val words: StateFlow<List<WordEntity>> = combine(
        repository.observeWordsBySurah(surah),
        refreshTrigger
    ) { list, _ -> list }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(word: WordEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(word.id, !word.isFavorite)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refreshWordsBySurah(surah)
            refreshTrigger.value = refreshTrigger.value + 1
        }
    }
}
