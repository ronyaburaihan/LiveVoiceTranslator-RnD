package com.example.livevoicetranslator_rd.data.source.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun createHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        createBaseHttpClientConfig()
    }
}