package com.example.skytah2.login.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.skytah2.login.data.network.model.LoginDto

@Entity(tableName = "data_table")
data class InfoDataEntity(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id") val id: Int = 0,
  @ColumnInfo(name = "mensaje")val mensaje: String = "",
  @ColumnInfo(name = "usuario")val usuario: String = "",
  @ColumnInfo(name = "telefono")val telefono: String = "",
  @ColumnInfo(name = "tagkey")val  tagkey: String = "",
  @ColumnInfo(name = "codigo")val  codigo: String = "",
  @ColumnInfo(name = "detail")val  detail: String = "",
  @ColumnInfo(name = "fecha")val   fecha: String = "",
  @ColumnInfo(name = "latitud")val latitud: Double = 0.0,
  @ColumnInfo(name = "longitud")val longitud: Double = 0.0
)

fun LoginDto.toDomain() = InfoDataEntity(id, mensaje, usuario, telefono, tagkey, codigo, detail, fecha, latitud, longitud)
