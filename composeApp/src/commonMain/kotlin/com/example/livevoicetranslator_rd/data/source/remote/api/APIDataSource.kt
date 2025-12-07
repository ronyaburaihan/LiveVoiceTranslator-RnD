package com.example.livevoicetranslator_rd.data.source.remote.api

import com.example.livevoicetranslator_rd.data.model.VisionApiRequest
import com.example.livevoicetranslator_rd.data.model.VisionApiResponse

interface APIDataSource {
    suspend fun callCloudVisionApi(
        request: VisionApiRequest
    ): VisionApiResponse
}