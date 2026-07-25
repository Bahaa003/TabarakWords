package com.tabarak.quranwords.ui.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarak.quranwords.data.WordEntity
import com.tabarak.quranwords.data.WordRepository
import com.tabarak.quranwords.ui.navigation.SOURCE_FAVORITES
import com.tabarak.quranwords.ui.navigation.SOURCE_RANDOM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlashcardViewModel(
    private val repository: WordRepository,
    private val source: String
) : ViewModel() {

    private val _words = MutableStateFlow<List<WordEntity>>(emptyList())
    val words: StateFlow<List<WordEntity>> = _words.asStateFlow()

    private val _index = MutableStateFlow(0)
    val index: StateFlow<Int> = _index.asStateFlow()

    private val _showMeaning = MutableStateFlow(false)
    val showMeaning: StateFlow<Boolean> = _showMeaning.asStateFlow()

    init {
        viewModelScope.launch {
            _words.value = when (source) {
                SOURCE_RANDOM -> repository.getRandomWords(30)
                SOURCE_FAVORITES -> repository.getFavoritesOnce()
                else -> repository.getWordsBySurah(source)
            }
        }
    }

    fun flip() {
        _showMeaning.value = !_showMeaning.value
        if (_showMeaning.value) {
            currentWord()?.let { word ->
                viewModelScope.launch { repository.markReviewed(word.id) }
            }
        }
    }

    fun currentWord(): WordEntity? = _words.value.getOrNull(_index.value)

    fun next() {
        if (_index.value < _words.value.lastIndex) {
            _index.value += 1
            _showMeaning.value = false
        }
    }

    fun previous() {
        if (_index.value > 0) {
            _index.value -= 1
            _showMeaning.value = false
        }
    }

    fun toggleFavoriteCurrent() {
        val word = currentWord() ?: return
        viewModelScope.launch {
            repository.toggleFavorite(word.id, !word.isFavorite)
            _words.value = _words.value.map {
                if (it.id == word.id) it.copy(isFavorite = !word.isFavorite) else it
            }
        }
    }
}
