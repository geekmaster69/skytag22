package com.example.skytah2.login.data.network

import com.example.skytah2.core.BaseRetrofit
import com.example.skytah2.login.data.network.model.LoginDto
import com.example.skytah2.login.data.network.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginService {

    private val retrofit = BaseRetrofit.getRetrofit()

    suspend fun getLogin(loginDto: LoginDto) :LoginResponse{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(LoginApiClient::class.java).getLoginApi(loginDto)
            response
        }
    }
}