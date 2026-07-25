package com.tabarak.quranwords.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarak.quranwords.data.WordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class StatsUiState(
    val total: Int = 0,
    val reviewed: Int = 0,
    val known: Int = 0,
    val unknown: Int = 0
) {
    val progressPercent: Int
        get() = if (total == 0) 0 else (reviewed * 100 / total)
}

class StatsViewModel(repository: WordRepository) : ViewModel() {

    val state: StateFlow<StatsUiState> = combine(
        repository.observeTotalCount(),
        repository.observeReviewedCount(),
        repository.observeKnownCount(),
        repository.observeUnknownCount()
    ) { total, reviewed, known, unknown ->
        StatsUiState(total = total, reviewed = reviewed, known = known, unknown = unknown)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())
}
