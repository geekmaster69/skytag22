package com.example.skytah2.ui.data.network

import com.example.skytah2.core.BaseRetrofit
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.data.network.model.InfoSOSResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SosService {
    private val retrofit = BaseRetrofit.getRetrofit()

    suspend fun senData(infoSOS: InfoSOS): InfoSOSResponse{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(TagKeyApiClient::class.java).sendInfoSos(infoSOS)
            response
        }
    }
}