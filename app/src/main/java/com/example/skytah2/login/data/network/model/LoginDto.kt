package com.example.skytah2.login.data.network.model

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("mensaje") val mensaje: String = "",
    @SerializedName("usuario")val usuario: String = "",
    @SerializedName("telefono")val telefono: String = "",
    @SerializedName("tagkey")val  tagkey: String = "",
    @SerializedName("codigo")val  codigo: String = "",
    @SerializedName("detail")val  detail: String = "",
    @SerializedName("fecha")val   fecha: String = "",
    @SerializedName("latitud")val latitud: Double = 0.0,
    @SerializedName("longitud")val longitud: Double = 0.0
)
