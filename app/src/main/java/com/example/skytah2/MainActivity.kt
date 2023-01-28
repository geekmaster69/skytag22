package com.example.skytah2

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.skytah2.databinding.ActivityMainBinding
import com.example.skytah2.ui.data.network.model.InfoSOS
import com.example.skytah2.ui.viewmodel.SendInfoViewModel
import com.polidea.rxandroidble3.RxBleDevice
import java.text.SimpleDateFormat
import java.util.*
@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity(), BLEView {
    private lateinit var dateFormat: SimpleDateFormat
    private var bleService: BLEService? = null
    private var bleServiceBound = false
    private val sendInfoViewModel: SendInfoViewModel by viewModels()

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
            Intent(applicationContext, BLEService::class.java).apply {
                action = BLEService.ACTION_START
                startService(this)
            }
        }

        binding.btnDesconectar.setOnClickListener {
            Intent(applicationContext, BLEService::class.java).apply {
                action = BLEService.ACTION_STOP
                startService(this)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (checkAllRequiredPermissions()) {
            if (!bleServiceBound) {
                val bleServiceIntent = Intent(applicationContext, BLEService::class.java)
                applicationContext.bindService(bleServiceIntent, bleServiceConnection, Context.BIND_AUTO_CREATE)
                applicationContext.startService(bleServiceIntent)
            }
        }
    }
    private fun checkAllRequiredPermissions(): Boolean {

        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,

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
    companion object {
        private const val REQUEST_ALL_PERMISSIONS = 1001
    }
}