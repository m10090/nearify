package com.example.nearify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class Device(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "wifi_mac") val wifiMac: String? = null,
    @ColumnInfo(name = "bluetooth_mac") val bluetoothMac: String? = null
) {
    init {
        require(wifiMac != null || bluetoothMac != null) {
            "Device must have a MAC address."
        }
    }
}
