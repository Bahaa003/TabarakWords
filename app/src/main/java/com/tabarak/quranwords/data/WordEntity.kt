package com.tabarak.quranwords.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * يمثل كلمة قرآنية واحدة مع معناها وحالة تقدم المستخدم فيها.
 */
@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey
    val id: Int,
    val juz: String,
    val surah: String,
    val word: String,
    val meaning: String,
    val needsReview: Boolean = false,

    // حالة تقدم المستخدم (تُحفظ تلقائياً محلياً)
    val isFavorite: Boolean = false,
    val isReviewed: Boolean = false,
    // null = لم تُختبر بعد، true = أعرفها، false = لا أعرفها
    val knownStatus: Boolean? = null,
    val reviewCount: Int = 0,
    val lastReviewedAt: Long = 0L
)
