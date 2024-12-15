package com.example.nearify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class Device(
    @PrimaryKey @ColumnInfo(name="bluetooth_mac") val bluetoothMac: String,
    val name: String,
    @ColumnInfo(name = "in_range") val inRange: Boolean = true,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (bluetoothMac != other.bluetoothMac) return false
        if (name != other.name) return false
        if (inRange != other.inRange) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bluetoothMac.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + inRange.hashCode()
        return result
    }
}
