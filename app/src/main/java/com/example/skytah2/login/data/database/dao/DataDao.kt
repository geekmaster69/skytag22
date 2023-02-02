package com.example.skytah2.login.data.database.dao

import androidx.room.*
import com.example.skytah2.login.data.database.entities.InfoDataEntity

@Dao
interface DataDao {
    @Query("SELECT * FROM data_table")

    suspend fun getAllData(): InfoDataEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllData(data: InfoDataEntity)

    @Update
    suspend fun updateData(data: InfoDataEntity)

    @Delete
    suspend fun deleteData(data: InfoDataEntity)

    @Query("DELETE FROM DATA_TABLE")
    suspend fun deleteAllData()

}