package com.example.skytah2.login.domain

import com.example.skytah2.login.data.LoginRepository
import com.example.skytah2.login.data.network.model.LoginDto
import com.example.skytah2.login.data.network.model.LoginResponse

class LoginUseCase {
    private val repository = LoginRepository()

    suspend operator fun invoke(loginDto: LoginDto) : LoginResponse{
        val data = repository.login(loginDto)
        return data
    }
}