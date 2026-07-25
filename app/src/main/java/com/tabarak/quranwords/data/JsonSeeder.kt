package com.tabarak.quranwords.data

import android.content.Context
import org.json.JSONArray

/**
 * يقرأ ملف assets/words.json الذي تم استخراجه من ملف تفسير جزأي عمّ وتبارك
 * ويحوّله إلى قائمة كائنات WordEntity لتعبئة قاعدة البيانات عند أول تشغيل للتطبيق.
 */
object JsonSeeder {

    fun loadWordsFromAssets(context: Context): List<WordEntity> {
        val jsonString = context.assets.open("words.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
        val array = JSONArray(jsonString)
        val result = ArrayList<WordEntity>(array.length())
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(
                WordEntity(
                    id = obj.getInt("id"),
                    juz = obj.getString("juz"),
                    surah = obj.getString("surah"),
                    word = obj.getString("word"),
                    meaning = obj.getString("meaning"),
                    needsReview = obj.optBoolean("needs_review", false)
                )
            )
        }
        return result
    }
}
