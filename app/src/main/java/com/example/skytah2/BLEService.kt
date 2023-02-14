package com.example.skytah2

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.os.ParcelUuid
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skytah2.location.DefaultLocationClient
import com.example.skytah2.location.LocationClient
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.data.network.model.InfoSOSResponse
import com.example.skytah2.ui.domain.SendInfoUseCase
import com.example.skytah2.ui.data.network.viewmodel.SendInfoViewModel
import com.google.android.gms.location.LocationServices
import com.polidea.rxandroidble3.NotificationSetupMode
import com.polidea.rxandroidble3.RxBleClient
import com.polidea.rxandroidble3.RxBleDevice
import com.polidea.rxandroidble3.scan.ScanFilter
import com.polidea.rxandroidble3.scan.ScanSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

class BLEService : Service() {

    private val TAG: String = BLEService::class.java.simpleName

    private lateinit var rxBleClient: RxBleClient
    private lateinit var locManager: LocationManager
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var deviceMac: String
    private val serviceUUID: ParcelUuid = ParcelUuid.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    private val characteristicUUID: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    private var view: BLEView? = null
    private val serviceBinder = ServiceBinder()
    private lateinit var lat: String
    private lateinit var long: String
    //Localizacion
    private val servicesScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    var sendInfoUseCase = SendInfoUseCase()

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service onCreate")
        rxBleClient = RxBleClient.create(applicationContext)
        locManager = getSystemService(LOCATION_SERVICE) as LocationManager
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service onStartCommand")

        when(intent?.action){
          //  ACTION_START -> starLocation()
          //  ACTION_STOP  -> stop()
            ACTION_START_SCAN -> scan()
        }
        return START_STICKY
    }

    private fun starLocation() {

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Rastreando")
            .setContentText("Ubicacion: Buscando...")
            .setSmallIcon(R.drawable.ic_location)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdate(10000L) //obtener el tiempo de actualizacion
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                lat = location.latitude.toString()
                long = location.longitude.toString()

                val smsManager = SmsManager.getDefault()
                val phoneNumber = "5519487452"
                val message = "Latitud:$lat \n Longitud: $long"
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.i("sendSMS", "SMS enviado")


                val mensaje = "reporte"
                val usuario = "tagkeyuser"
                val telefono = "5611750632"
                val tagKey = deviceMac ?: "No Disponible"
                val codigo = "1"
                val datail = "reporte de tag"
                val date = Date()
                val fecha = dateFormat.format(date)
                val latitud = lat
                val longitud = long

                val response =  sendInfoUseCase(InfoSOS(mensaje, usuario, telefono, tagKey, codigo, datail, fecha, latitud.toDouble(), longitud.toDouble()))

                Log.i("Responce", response.toString())

                Log.i("Fecha", "$fecha  $latitud  $longitud")

                val updateNotification = notification.setContentText(
                    "Ubicacion: $lat, $long"
                )
                notificationManager.notify(1, updateNotification.build())
            }
            .launchIn(servicesScope)
        startForeground(1, notification.build())


    }


    override fun onBind(intent: Intent?): IBinder {
        Log.i(TAG, "Service onBind")
        return serviceBinder
    }

    fun bindView(view: BLEView) {
        this.view = view
    }

    fun scan() {
        rxBleClient.scanBleDevices(scanSettings(), scanFilter())
            .firstElement()
            .subscribe(
                { scanResult ->
                   connect(scanResult.bleDevice)
                }, onError())
    }

    private fun connect(bleDevice: RxBleDevice) {
        view?.onConnected(bleDevice)
        bleDevice.establishConnection(false)
            .subscribe({rxBleConnection ->
                rxBleConnection.setupIndication(characteristicUUID, NotificationSetupMode.COMPAT)
                    .subscribe({ observable ->
                        observable.subscribe({
                            view?.onKeyPressed()
                            pressKey()
                        },onError())
                    },onError())
            },onError())
    }

    private fun scanSettings(): ScanSettings =
        ScanSettings.Builder()
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

    private fun scanFilter(): ScanFilter =
        ScanFilter.Builder()
            .setServiceUuid(serviceUUID)
            .build()


    private fun pressKey() {
        view?.onKeyPressed()
        starLocation()

    }

    private fun onError(): (Throwable) -> Unit {
        return { throwable ->
            throwable.message?.let { Log.e(TAG, it) }
            view?.onError(throwable)
        }
    }

    inner class ServiceBinder : Binder() {
        internal val service: BLEService
            get() = this@BLEService
    }

    override fun onDestroy() {
        super.onDestroy()
        servicesScope.cancel()
    }

    companion object{
        const val ACTION_START = "ACTION START"
        const val ACTION_STOP = "ACTION STOP"
        const val ACTION_START_SCAN = "ACTION STAR SCAN"
    }
}