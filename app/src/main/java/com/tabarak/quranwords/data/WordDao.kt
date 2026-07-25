package com.tabarak.quranwords.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(words: List<WordEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: WordEntity)

    @Query("SELECT COUNT(*) FROM words")
    suspend fun count(): Int

    @Query("SELECT * FROM words ORDER BY id ASC")
    fun observeAll(): Flow<List<WordEntity>>

    @Query("SELECT DISTINCT juz FROM words")
    suspend fun getJuzList(): List<String>

    @Query("SELECT surah FROM words WHERE juz = :juz GROUP BY surah ORDER BY MIN(id)")
    suspend fun getSurahsForJuz(juz: String): List<String>

    @Query("SELECT * FROM words WHERE surah = :surah ORDER BY id ASC")
    fun observeWordsBySurah(surah: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE surah = :surah ORDER BY id ASC")
    suspend fun getWordsBySurah(surah: String): List<WordEntity>

    @Query("SELECT * FROM words ORDER BY id ASC")
    suspend fun getAllWordsOnce(): List<WordEntity>

    @Query("SELECT * FROM words WHERE isFavorite = 1 ORDER BY id ASC")
    fun observeFavorites(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE isFavorite = 1 ORDER BY id ASC")
    suspend fun getFavoritesOnce(): List<WordEntity>

    @Query("SELECT * FROM words WHERE word LIKE '%' || :query || '%' OR meaning LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchWords(query: String): Flow<List<WordEntity>>

    @Update
    suspend fun update(word: WordEntity)

    @Query("UPDATE words SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Int, isFavorite: Boolean)

    @Query("UPDATE words SET isReviewed = 1, reviewCount = reviewCount + 1, lastReviewedAt = :time WHERE id = :id")
    suspend fun markReviewed(id: Int, time: Long)

    @Query("UPDATE words SET knownStatus = :known, isReviewed = 1, reviewCount = reviewCount + 1, lastReviewedAt = :time WHERE id = :id")
    suspend fun setKnownStatus(id: Int, known: Boolean, time: Long)

    // إحصائيات
    @Query("SELECT COUNT(*) FROM words")
    fun observeTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM words WHERE isReviewed = 1")
    fun observeReviewedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM words WHERE knownStatus = 1")
    fun observeKnownCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM words WHERE knownStatus = 0")
    fun observeUnknownCount(): Flow<Int>

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomWords(limit: Int): List<WordEntity>
}
