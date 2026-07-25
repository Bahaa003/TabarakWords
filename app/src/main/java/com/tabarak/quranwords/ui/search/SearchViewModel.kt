package com.tabarak.quranwords.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarak.quranwords.data.WordEntity
import com.tabarak.quranwords.data.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: WordRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val results: StateFlow<List<WordEntity>> = _query
        .flatMapLatest { q ->
            if (q.isBlank()) flowOf(emptyList()) else repository.searchWords(q.trim())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun toggleFavorite(word: WordEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(word.id, !word.isFavorite)
        }
    }
}
