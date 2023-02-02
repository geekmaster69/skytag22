package com.example.skytah2.login.data.network.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("estado")val estado: Int = 0,
    @SerializedName("mensaje")val mensaje: String = ""
)
