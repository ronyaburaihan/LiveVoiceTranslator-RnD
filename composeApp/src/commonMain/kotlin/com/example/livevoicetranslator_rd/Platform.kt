package com.example.livevoicetranslator_rd

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform