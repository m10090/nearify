package com.example.nearify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class Device(
    @PrimaryKey @ColumnInfo(name="bluetooth_mac") val bluetoothMac: String,
    val name: String,
    @ColumnInfo(name = "in_range") val inRange: Boolean = true,
)
