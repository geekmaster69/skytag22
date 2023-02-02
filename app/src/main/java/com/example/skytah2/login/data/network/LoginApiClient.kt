package com.example.skytah2.login.data.network

import androidx.lifecycle.LiveData
import com.example.skytah2.core.Constans
import com.example.skytah2.login.data.network.model.LoginDto
import com.example.skytah2.login.data.network.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiClient {

    @POST(Constans.TAG_KEY_SERVICE_PATH)
    suspend fun getLoginApi(
        @Body data: LoginDto
    ): LoginResponse
}