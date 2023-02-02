package com.example.skytah2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.skytah2.databinding.ActivityMainBinding
import com.example.skytah2.workers.WorkerLocation
import com.polidea.rxandroidble3.RxBleDevice
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
private const val ENABLE_BLUETOOTH_REQUEST = 1
private const val REQUEST_ALL_PERMISSIONS = 1001
class MainActivity : AppCompatActivity(), BLEView {
    private var bleService: BLEService? = null
    private var bleServiceBound = false
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    //connect to the service
    private val bleServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? BLEService.ServiceBinder
            bleService = binder?.service
            bleService?.bindView(this@MainActivity)
            bleServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bleServiceBound = false
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var locManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        locManager = getSystemService(LOCATION_SERVICE) as LocationManager

        binding.btnConectar.setOnClickListener {

        }

        binding.btnStarGPS.setOnClickListener {
         myPeriodicWork()

        }

        binding.btnDesconectar.setOnClickListener {
            Intent(applicationContext, BLEService::class.java).apply {
                action = BLEService.ACTION_STOP
                startService(this)
            }
        }

        getBatteryPercentage()
    }

    private fun myPeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(true)
            .build()

        val myWorkRequest = PeriodicWorkRequest.Builder(
            WorkerLocation::class.java,
            15,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .addTag("my_id")
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.REPLACE, myWorkRequest)


    }


    private fun getBatteryPercentage() {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
            this.registerReceiver(null, intentFilter)
        }
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = level/scale.toFloat()
        binding.tvBatteryLevel.text = "%" + (batteryPct*100).toString()


    }

    override fun onResume() {
        super.onResume()

        if (checkAllRequiredPermissions()) {
            if (!bluetoothAdapter.isEnabled){
                activateCommunications()
                if (!bleServiceBound) {
                    val bleServiceIntent = Intent(applicationContext, BLEService::class.java)
                    applicationContext.bindService(bleServiceIntent, bleServiceConnection, Context.BIND_AUTO_CREATE)
                    applicationContext.startService(bleServiceIntent)
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun activateCommunications() {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo

        if (!bluetoothAdapter.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST)
        }
    }

    private fun checkAllRequiredPermissions(): Boolean {

        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION

            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.SEND_SMS)
        }

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_ALL_PERMISSIONS)
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ALL_PERMISSIONS -> finishIfRequiredPermissionsNotGranted(grantResults)
            else -> {
            }
        }
    }
    private fun finishIfRequiredPermissionsNotGranted(grantResults: IntArray) {
        if (grantResults.isNotEmpty()) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Se requieren todos los permisos", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Se requieren todos los permisos", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onConnected(bleDevice: RxBleDevice) {
        runOnUiThread {
            Toast.makeText(this, "Conectado con: ${bleDevice.macAddress}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onKeyPressed() {
        runOnUiThread {
         Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Intent(applicationContext, BLEService::class.java).apply {
            action = BLEService.ACTION_STOP
            startService(this)
        }
    }

    override fun onError(throwable: Throwable) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ENABLE_BLUETOOTH_REQUEST ->{
                if (resultCode != Activity.RESULT_OK){
                    activateCommunications()
                }
            }
        }
    }
}