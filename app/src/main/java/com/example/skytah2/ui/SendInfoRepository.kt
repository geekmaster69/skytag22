package com.example.skytah2.ui

import com.example.skytah2.ui.data.network.SosService
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.data.network.model.InfoSOSResponse

class SendInfoRepository {

    private val api = SosService()

    suspend fun sendInfo(infoSOS: InfoSOS): InfoSOSResponse{
        return api.senData(infoSOS)
    }
}