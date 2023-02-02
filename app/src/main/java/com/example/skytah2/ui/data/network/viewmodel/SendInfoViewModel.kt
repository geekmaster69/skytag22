package com.example.skytah2.ui.data.network.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.data.network.model.InfoSOSResponse
import com.example.skytah2.ui.domain.SendInfoUseCase
import kotlinx.coroutines.launch

class SendInfoViewModel: ViewModel() {
    val senDataModel = MutableLiveData<InfoSOSResponse>()
    var sendInfoUseCase = SendInfoUseCase()

//    fun onsendInfio(infoSOS: InfoSOS){
//        viewModelScope.launch {
//            val result = sendInfoUseCase(infoSOS)
//            senDataModel.postValue(result)
//
//        }
//    }
}