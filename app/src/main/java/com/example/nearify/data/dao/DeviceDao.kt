package com.example.nearify.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nearify.data.model.Device


@Dao
interface DeviceDao {
    @Insert
    fun insert(device: Device)

    @get:Query("SELECT * FROM device")
    val getAllDevice: List<Device>

    @Query("Delete from device where id = :id")
    fun deleteDevice(id: Int)


}