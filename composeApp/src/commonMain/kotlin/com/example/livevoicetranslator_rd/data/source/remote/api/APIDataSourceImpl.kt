package com.example.livevoicetranslator_rd.data.source.remote.api

import com.example.livevoicetranslator_rd.data.model.VisionApiRequest
import com.example.livevoicetranslator_rd.data.model.VisionApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class APIDataSourceImpl(
    private val httpClient: HttpClient,
) : APIDataSource {

    override suspend fun callCloudVisionApi(
        request: VisionApiRequest
    ): VisionApiResponse {
        val key = ""
        val baseUrl = "https://vision.googleapis.com/v1/images:annotate?key=$key"
        return postRequest(baseUrl, request)
    }

    private suspend inline fun <reified T> postRequest(
        url: String,
        body: Any
    ): T = try {
        println("Calling Cloud Vision API...${body}")
        httpClient.post(url) {
            setBody(body)
        }.body()
    } catch (e: Exception) {
        println("Vision API Error: ${e.message}")
        throw Exception("Please try again later", e)
    }
}