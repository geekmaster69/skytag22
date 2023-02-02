package com.example.skytah2.login.data

import com.example.skytah2.login.data.network.LoginService
import com.example.skytah2.login.data.network.model.LoginDto
import com.example.skytah2.login.data.network.model.LoginResponse

class LoginRepository {
    private val api = LoginService()

    suspend fun login(loginDto: LoginDto) : LoginResponse{
        return api.getLogin(loginDto)
    }
}