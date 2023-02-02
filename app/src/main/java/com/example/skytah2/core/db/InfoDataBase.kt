package com.example.skytah2.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skytah2.login.data.database.dao.DataDao
import com.example.skytah2.login.data.database.entities.InfoDataEntity

@Database(entities = [InfoDataEntity::class], version = 1)
abstract class InfoDataBase : RoomDatabase(){

    abstract fun dataDao() : DataDao
}