package com.tabarak.quranwords.ui.quiz

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

class QuizViewModel(
    private val repository: WordRepository,
    private val source: String
) : ViewModel() {

    private val _words = MutableStateFlow<List<WordEntity>>(emptyList())
    val words: StateFlow<List<WordEntity>> = _words.asStateFlow()

    private val _index = MutableStateFlow(0)
    val index: StateFlow<Int> = _index.asStateFlow()

    private val _revealed = MutableStateFlow(false)
    val revealed: StateFlow<Boolean> = _revealed.asStateFlow()

    private val _knownCount = MutableStateFlow(0)
    val knownCount: StateFlow<Int> = _knownCount.asStateFlow()

    private val _unknownCount = MutableStateFlow(0)
    val unknownCount: StateFlow<Int> = _unknownCount.asStateFlow()

    private val _finished = MutableStateFlow(false)
    val finished: StateFlow<Boolean> = _finished.asStateFlow()

    init {
        viewModelScope.launch {
            _words.value = when (source) {
                SOURCE_RANDOM -> repository.getRandomWords(30)
                SOURCE_FAVORITES -> repository.getFavoritesOnce()
                else -> repository.getWordsBySurah(source)
            }
        }
    }

    fun currentWord(): WordEntity? = _words.value.getOrNull(_index.value)

    /** يعرض المعنى دون تسجيل إجابة */
    fun reveal() {
        _revealed.value = true
    }

    /** يسجل إجابة المستخدم (أعرفها / لا أعرفها) وينتقل للكلمة التالية */
    fun answer(knows: Boolean) {
        val word = currentWord() ?: return
        viewModelScope.launch {
            repository.setKnownStatus(word.id, knows)
        }
        if (knows) _knownCount.value += 1 else _unknownCount.value += 1
        _revealed.value = true
    }

    fun next() {
        if (_index.value < _words.value.lastIndex) {
            _index.value += 1
            _revealed.value = false
        } else {
            _finished.value = true
        }
    }

    fun restart() {
        _index.value = 0
        _revealed.value = false
        _knownCount.value = 0
        _unknownCount.value = 0
        _finished.value = false
    }
}
