package com.example.skytah2.workers

import android.content.Context
import android.nfc.Tag
import android.util.Log
import android.util.Log.i
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.skytah2.location.DefaultLocationClient
import com.example.skytah2.location.LocationClient
import com.example.skytah2.ui.domain.SendInfoUseCase
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WorkerLocation(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {

        val locationClient: LocationClient = DefaultLocationClient(
            applicationContext, LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        var sendInfoUseCase = SendInfoUseCase()

        locationClient
            .getLocationUpdate(1L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                makeStatusNotification("Latitud: $lat Longitud: $long", applicationContext)
            }
            .launchIn(CoroutineScope(SupervisorJob() + Dispatchers.IO))

        return Result.success()
    }
}
