package com.example.skytah2.location

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import com.example.skytah2.core.db.InfoDataBase

class LocationApp: Application() {

    companion object{
        lateinit var dataBase: InfoDataBase
    }
    override fun onCreate() {
        super.onCreate()
        val chanel = NotificationChannel(
            "location",
            "Location",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(chanel)

        dataBase = Room.databaseBuilder(
            this,
            InfoDataBase::class.java,
            "InfoDatabase")
            .build()
    }
}