package com.example.skytah2

import com.polidea.rxandroidble3.RxBleClient
import kotlinx.coroutines.flow.Flow

interface BluetoothClientInterface {

    fun getClickListener()

    class LocationException(message: String): Exception()
}