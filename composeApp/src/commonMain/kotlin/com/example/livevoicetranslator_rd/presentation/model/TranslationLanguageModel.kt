package com.example.livevoicetranslator_rd.presentation.model

import com.example.livevoicetranslator_rd.domain.model.TranslateLanguage

enum class TranslateLanguageModel(
    val domain: TranslateLanguage,
    val label: String,
    val code: String
) {

    AFRIKAANS(TranslateLanguage.AFRIKAANS, "Afrikaans", "af"),
    ARABIC(TranslateLanguage.ARABIC, "Arabic", "ar"),
    BELARUSIAN(TranslateLanguage.BELARUSSIAN, "Belarusian", "be"),
    BULGARIAN(TranslateLanguage.BULGARIAN, "bulgarian", "bg"),
    BENGALI(TranslateLanguage.BENGALI, "Bengali", "bn"),
    CATALAN(TranslateLanguage.CATALAN, "Catalan", "ca"),
    CZECH(TranslateLanguage.CZECH, "Czech", "cs"),
    WELSH(TranslateLanguage.WELSH, "Welsh", "cy"),
    DANISH(TranslateLanguage.DANISH, "Danish", "da"),
    GERMAN(TranslateLanguage.GERMAN, "Derman", "de"),
    GREEK(TranslateLanguage.GREEK, "Greek", "el"),
    ENGLISH(TranslateLanguage.ENGLISH, "English", "en"),
    ESPERANTO(TranslateLanguage.ESPERANTO, "Esperanto", "eo"),
    SPANISH(TranslateLanguage.SPANISH, "Spanish", "es"),
    ESTONIAN(TranslateLanguage.ESTONIAN, "Estonian", "et"),
    PERSIAN(TranslateLanguage.PERSIAN, "Persian", "fa"),
    FINNISH(TranslateLanguage.FINNISH, "Finnish", "fi"),
    FRENCH(TranslateLanguage.FRENCH, "French", "fr"),
    IRISH(TranslateLanguage.IRISH, "Irish", "ga"),
    GALICIAN(TranslateLanguage.GALICIAN, "Galician", "gl"),
    GUJARATI(TranslateLanguage.GUJARATI, "Gujarati", "gu"),
    HEBREW(TranslateLanguage.HEBREW, "Hebrew", "he"),
    HINDI(TranslateLanguage.HINDI, "Hindi", "hi"),
    CROATIAN(TranslateLanguage.CROATIAN, "Croatian", "hr"),
    HAITIAN(TranslateLanguage.HAITIAN, "Haitian", "ht"),
    HUNGARIAN(TranslateLanguage.HUNGARIAN, "Hungarian", "hu"),
    INDONESIAN(TranslateLanguage.INDONESIAN, "Indonesian", "id"),
    ICELANDIC(TranslateLanguage.ICELANDIC, "Icelandic", "is"),
    ITALIAN(TranslateLanguage.ITALIAN, "Italian", "it"),
    JAPANESE(TranslateLanguage.JAPANESE, "Japanese", "ja"),
    GEORGIAN(TranslateLanguage.GEORGIAN, "Georgian", "ka"),
    KANNADA(TranslateLanguage.KANNADA, "Kannada", "kn"),
    KOREAN(TranslateLanguage.KOREAN, "Korean", "ko"),
    LITHUANIAN(TranslateLanguage.LITHUANIAN, "Lithuanian", "lt"),
    LATVIAN(TranslateLanguage.LATVIAN, "Latvian", "lv"),
    MACEDONIAN(TranslateLanguage.MACEDONIAN, "Macedonian", "mk"),
    MARATHI(TranslateLanguage.MARATHI, "Marathi", "mr"),
    MALAY(TranslateLanguage.MALAY, "Malay", "ms"),
    MALTESE(TranslateLanguage.MALTESE, "Maltese", "mt"),
    DUTCH(TranslateLanguage.DUTCH, "Dutch", "nl"),
    NORWEGIAN(TranslateLanguage.NORWEGIAN, "Norwegian", "no"),
    POLISH(TranslateLanguage.POLISH, "Polish", "pl"),
    PORTUGUESE(TranslateLanguage.PORTUGUESE, "Portuguese", "pt"),
    ROMANIAN(TranslateLanguage.ROMANIAN, "Romanian", "ro"),
    RUSSIAN(TranslateLanguage.RUSSIAN, "Russian", "ru"),
    SLOVAK(TranslateLanguage.SLOVAK, "Slovak", "sk"),
    SLOVENIAN(TranslateLanguage.SLOVENIAN, "Slovenian", "sl"),
    ALBANIAN(TranslateLanguage.ALBANIAN, "Albanian", "sq"),
    SWEDISH(TranslateLanguage.SWEDISH, "Swedish", "sv"),
    SWAHILI(TranslateLanguage.SWAHILI, "Swahili", "sw"),
    TAMIL(TranslateLanguage.TAMIL, "Tamil", "ta"),
    TELUGU(TranslateLanguage.TELUGU, "Telugu", "te"),
    THAI(TranslateLanguage.THAI, "Thai", "th"),
    TAGALOG(TranslateLanguage.TAGALOG, "Tagalog", "tl"),
    TURKISH(TranslateLanguage.TURKISH, "Turkish", "tr"),
    UKRAINIAN(TranslateLanguage.UKRAINIAN, "Ukrainian", "uk"),
    URDU(TranslateLanguage.URDU, "Urdu", "ur"),
    VIETNAMESE(TranslateLanguage.VIETNAMESE, "Vietnamese", "vi"),
    CHINESE(TranslateLanguage.CHINESE, "Chinese", "zh");

    companion object {
        fun fromCode(code: String): TranslateLanguageModel? =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) }

        fun fromDomain(language: TranslateLanguage): TranslateLanguageModel =
            entries.first { it.domain == language }
    }
}
