package com.tabarak.quranwords

import android.app.Application
import com.tabarak.quranwords.data.WordRepository

class QuranWordsApp : Application() {

    lateinit var repository: WordRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = WordRepository(this)
    }
}
