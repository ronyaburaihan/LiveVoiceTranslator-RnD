package com.example.livevoicetranslator_rd.domain.util

object LanguageTiers {
    val TIER_A = setOf(
        "en","es","fr","de","it","pt","nl","ru","tr","pl",
        "zh","ja","ko","hi","bn","ur","ar","fa","he","id",
        "vi","th","ms"
    )

    val TIER_B = setOf(
        "sv","no","da","fi","cs","sk","hu","ro","bg","sl",
        "hr","sr","bs","al","lt","lv","ee",
        "ta","te","kn","ml","mr","gu","pa","si","ne","fil",
        "my","km","lo","sw","am","so","af","ku","az","hy",
        "ka","kk","uz"
    )

    fun isTierA(lang: String?) = lang?.let { TIER_A.contains(it.lowercase()) } ?: false
    fun isTierB(lang: String?) = lang?.let { TIER_B.contains(it.lowercase()) } ?: false
}