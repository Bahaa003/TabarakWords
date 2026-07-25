package com.tabarak.quranwords.ui.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarak.quranwords.data.WordEntity
import com.tabarak.quranwords.data.WordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WordListViewModel(private val repository: WordRepository, private val surah: String) : ViewModel() {

    val words: StateFlow<List<WordEntity>> = repository.observeWordsBySurah(surah)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(word: WordEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(word.id, !word.isFavorite)
        }
    }
}
