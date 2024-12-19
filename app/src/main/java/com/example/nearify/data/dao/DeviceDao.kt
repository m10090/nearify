package com.example.nearify.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.nearify.data.model.Device
import com.example.nearify.data.model.DeviceWithAction


@Dao
interface DeviceDao {

    @Insert
    fun insert(device: Device)

    @get:Query("SELECT * FROM device")
    val getAllDevices: List<Device>

    @Query("Delete from device where bluetooth_mac = :bluetoothMac")
    fun deleteDevice(bluetoothMac: String)

    @Delete
    fun deleteDevice(device: Device)

    @Query("SELECT name from device where bluetooth_mac = :bluetoothMac")
    fun getDevice(bluetoothMac: String):List<String>

    @Query("UPDATE device SET in_range = :in_range where bluetooth_mac = :bluetoothMac")
    fun updateDevice(bluetoothMac: String, in_range: Boolean)

    @Update
    fun updateDevice(device: Device)

    @get:Query("SELECT * FROM device where in_range")
    val getInRangeDevices: List<DeviceWithAction>

    @get:Query("SELECT * FROM device where not in_range")
    val getOutRangeDevices: List<DeviceWithAction>


}