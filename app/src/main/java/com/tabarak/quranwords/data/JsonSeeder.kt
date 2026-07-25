package com.tabarak.quranwords.data

import android.content.Context
import org.json.JSONArray

/**
 * يقرأ ملف assets/words.json الذي تم استخراجه من ملف تفسير جزأي عمّ وتبارك
 * ويحوّله إلى قائمة كائنات WordEntity لتعبئة قاعدة البيانات عند أول تشغيل للتطبيق.
 */
object JsonSeeder {

    fun loadWordsFromAssets(context: Context): List<WordEntity> {
        return try {
            val jsonString = context.assets.open("words.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText() }

            val array = JSONArray(jsonString)
            val result = ArrayList<WordEntity>(array.length())

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)

                val id = obj.optInt("id", 0).takeIf { it > 0 } ?: (1000000 + i)
                val juz = obj.optString("juz", "غير محدد").trim()
                val surah = obj.optString("surah", "غير محدد").trim()
                val word = obj.optString("word", "").trim()
                val meaning = obj.optString("meaning", "").trim()
                val needsReview = obj.optBoolean("needs_review", false)

                if (word.isEmpty() || meaning.isEmpty()) {
                    continue
                }

                result.add(
                    WordEntity(
                        id = id,
                        juz = juz,
                        surah = surah,
                        word = word,
                        meaning = meaning,
                        needsReview = needsReview
                    )
                )
            }

            result
        } catch (_: Exception) {
            emptyList()
        }
    }
}
