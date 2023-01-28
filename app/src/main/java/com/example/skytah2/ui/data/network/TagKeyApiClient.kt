package com.example.skytah2.ui.data.network

import com.example.skytah2.core.Constans
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.data.network.model.InfoSOSResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TagKeyApiClient {

    @POST(Constans.TAG_KEY_SERVICE_PATH)
    suspend fun sendInfoSos(@Body data: InfoSOS) : InfoSOSResponse
}