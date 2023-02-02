package com.example.skytah2.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skytah2.login.data.network.model.LoginDto
import com.example.skytah2.login.data.network.model.LoginResponse
import com.example.skytah2.login.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    val loginModel = MutableLiveData<LoginResponse>()
    var getLoginUseCase = LoginUseCase()

    fun onLogin(loginDto: LoginDto){
        viewModelScope.launch {
            val result = getLoginUseCase(loginDto)
            loginModel.postValue(result)
        }
    }
}