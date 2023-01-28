package com.example.skytah2.ui.domain

import com.example.skytah2.ui.SendInfoRepository
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.data.network.model.InfoSOSResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendInfoUseCase {
    private val respositori = SendInfoRepository()

    suspend operator fun invoke(infoSOS: InfoSOS) : InfoSOSResponse{
        val data = respositori.sendInfo(infoSOS)
        return data
    }
}