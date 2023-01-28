package com.example.skytah2

import android.content.Context
import com.polidea.rxandroidble3.RxBleClient
import kotlinx.coroutines.flow.callbackFlow

class BluetoothClient(
    private val context: Context,
    private val client: RxBleClient
):BluetoothClientInterface {
    override fun getClickListener() {
        if (context.hasLocationPermissions()){
            throw BluetoothClientInterface.LocationException("Missing location Permissions")
        }

        val rxBleClient = RxBleClient.create(context)


    }


}
