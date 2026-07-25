package com.tabarak.quranwords.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tabarak.quranwords.QuranWordsApp
import com.tabarak.quranwords.data.WordRepository

/**
 * يوفر الوصول إلى مستودع البيانات (Repository) من أي Composable.
 */
@Composable
fun rememberRepository(): WordRepository {
    val context = LocalContext.current
    val app = context.applicationContext as QuranWordsApp
    return app.repository
}

class GenericViewModelFactory<T : ViewModel>(
    private val creator: () -> T
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}

@Composable
inline fun <reified T : ViewModel> viewModelWithRepo(crossinline creator: (WordRepository) -> T): T {
    val repo = rememberRepository()
    return viewModel<T>(factory = GenericViewModelFactory { creator(repo) })
}
