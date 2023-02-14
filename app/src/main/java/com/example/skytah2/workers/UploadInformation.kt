package com.example.skytah2.workers

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.domain.SendInfoUseCase
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*

class UploadInformation(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {

    private var fusedLocationProviderClient: FusedLocationProviderClient?=null
    private var locationCallback: LocationCallback?=null
    private var locationRequest: LocationRequest?=null

    private var location: Location?=null

    override suspend fun doWork(): Result {


//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
//        locationRequest =
//            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000)
//                .setIntervalMillis(5000).build()
//
//        locationCallback = object : LocationCallback(){
//            override fun onLocationAvailability(p0: LocationAvailability) {
//                super.onLocationAvailability(p0)
//            }
//
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//                onNewLocation(locationResult)
//                Log.i("Ubicacion", "${locationResult.lastLocation}")
//            }
//        }
//        createLocationRequest(

        val sendInfoUseCase = SendInfoUseCase()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val fecha = dateFormat.format(Date())
        val mensaje = "reporte"
        val usuario = "tagkeyuser"
        val telefono = "5611750632"
        val tagKey ="FF:FF:40:01:2F:3F"
        val codigo = "1"
        val datail = "reporte de tag"

        val response = sendInfoUseCase.invoke(InfoSOS(
            mensaje = mensaje,
            fecha = fecha,
            usuario = usuario,
            telefono = telefono,
            tagkey = tagKey,
            codigo = codigo, detail = datail, longitud = 11.03535636, latitud = 43.245635674785478))

        Log.e("Responce", response.toString())

        makeStatusNotification("Ubicaion Actualizada", applicationContext)

        return Result.success()
    }


}
