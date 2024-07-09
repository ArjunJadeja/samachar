package com.arjun.samachar.utils

import com.arjun.samachar.data.model.Language

object LanguageHelper {

    private val languages = listOf(
        Language("ar", "Arabic", "العربية"),
        Language("de", "German", "Deutsch"),
        Language("en", "English", "English"),
        Language("es", "Spanish", "Español"),
        Language("fr", "French", "Français"),
        Language("he", "Hebrew", "עברית"),
        Language("it", "Italian", "Italiano"),
        Language("nl", "Dutch", "Nederlands"),
        Language("no", "Norwegian", "Norsk"),
        Language("pt", "Portuguese", "Português"),
        Language("ru", "Russian", "Русский"),
        Language("sv", "Swedish", "Svenska"),
        Language("zh", "Chinese", "中文")
    )

    fun getAllLanguages(): List<Language> {
        return languages
    }

}