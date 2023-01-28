package com.example.skytah2.ui.data.network.model

import com.google.gson.annotations.SerializedName

data class InfoSOSResponse(
    @SerializedName("estado")val estado: Int = 0,
    @SerializedName("mensaje")val mensaje: String = ""
)
