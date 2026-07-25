package com.tabarak.quranwords.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

/**
 * يوفر واجهة موحدة للتعامل مع بيانات الكلمات، ويهتم بتعبئة قاعدة البيانات
 * لأول مرة من ملف JSON المرفق ضمن أصول التطبيق (assets).
 */
class WordRepository(context: Context) {

    private val appContext = context.applicationContext
    private val db = AppDatabase.getInstance(appContext)
    private val dao = db.wordDao()

    suspend fun ensureSeeded() {
        val words = JsonSeeder.loadWordsFromAssets(appContext)
        if (words.isNotEmpty()) {
            dao.insertAll(words)
        }
    }

    fun observeAll(): Flow<List<WordEntity>> = dao.observeAll()

    suspend fun getJuzList(): List<String> = dao.getJuzList()

    suspend fun getSurahsForJuz(juz: String): List<String> = dao.getSurahsForJuz(juz)

    fun observeWordsBySurah(surah: String): Flow<List<WordEntity>> = dao.observeWordsBySurah(surah)

    suspend fun getWordsBySurah(surah: String): List<WordEntity> = dao.getWordsBySurah(surah)

    suspend fun refreshWordsBySurah(surah: String) {
        val current = dao.getWordsBySurah(surah)
        if (current.isEmpty()) {
            val words = JsonSeeder.loadWordsFromAssets(appContext)
            dao.insertAll(words.filter { it.surah == surah })
        }
    }

    suspend fun getAllWordsOnce(): List<WordEntity> = dao.getAllWordsOnce()

    suspend fun addCustomWord(juz: String, word: String, meaning: String) {
        val generatedId = (System.currentTimeMillis() % 1_000_000_000L).toInt()
        val defaultSurah = when (juz) {
            "جزء عم" -> "سورة النبأ"
            else -> "سورة الملك"
        }
        val entity = WordEntity(
            id = generatedId,
            juz = juz,
            surah = defaultSurah,
            word = word.trim(),
            meaning = meaning.trim(),
            needsReview = true
        )
        dao.insert(entity)
    }

    fun observeFavorites(): Flow<List<WordEntity>> = dao.observeFavorites()

    suspend fun getFavoritesOnce(): List<WordEntity> = dao.getFavoritesOnce()

    fun searchWords(query: String): Flow<List<WordEntity>> = dao.searchWords(query)

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) = dao.setFavorite(id, isFavorite)

    suspend fun markReviewed(id: Int) = dao.markReviewed(id, System.currentTimeMillis())

    suspend fun setKnownStatus(id: Int, known: Boolean) =
        dao.setKnownStatus(id, known, System.currentTimeMillis())

    fun observeTotalCount(): Flow<Int> = dao.observeTotalCount()
    fun observeReviewedCount(): Flow<Int> = dao.observeReviewedCount()
    fun observeKnownCount(): Flow<Int> = dao.observeKnownCount()
    fun observeUnknownCount(): Flow<Int> = dao.observeUnknownCount()

    suspend fun getRandomWords(limit: Int): List<WordEntity> = dao.getRandomWords(limit)
}
