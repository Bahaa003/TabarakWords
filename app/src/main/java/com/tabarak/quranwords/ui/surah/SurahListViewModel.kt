package com.tabarak.quranwords.ui.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarak.quranwords.data.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SurahRow(val name: String, val wordCount: Int)

class SurahListViewModel(private val repository: WordRepository, private val juz: String) : ViewModel() {

    private val _surahs = MutableStateFlow<List<SurahRow>>(emptyList())
    val surahs: StateFlow<List<SurahRow>> = _surahs.asStateFlow()

    init {
        viewModelScope.launch {
            val names = repository.getSurahsForJuz(juz)
            val rows = names.map { name ->
                SurahRow(name, repository.getWordsBySurah(name).size)
            }
            _surahs.value = rows
        }
    }
}
