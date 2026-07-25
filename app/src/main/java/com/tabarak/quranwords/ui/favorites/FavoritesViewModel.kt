package com.tabarak.quranwords.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarak.quranwords.data.WordEntity
import com.tabarak.quranwords.data.WordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: WordRepository) : ViewModel() {

    val favorites: StateFlow<List<WordEntity>> = repository.observeFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeFromFavorites(word: WordEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(word.id, false)
        }
    }
}
