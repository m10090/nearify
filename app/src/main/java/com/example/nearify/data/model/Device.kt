package com.example.nearify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable


@Entity(tableName = "device")
class Device(
    @PrimaryKey @ColumnInfo(name = "bluetooth_mac") val bluetoothMac: String,
    val name: String,
    @ColumnInfo(name = "in_range") val inRange: Boolean = true,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (bluetoothMac != other.bluetoothMac) return false
        if (name != other.name) return false
        if (inRange != other.inRange) return false

        return true
    }

    fun copy(
        bluetoothMac: String = this.bluetoothMac,
        name: String = this.name,
        inRange: Boolean = this.inRange
    ): Device {
        return Device(bluetoothMac, name, inRange)
    }

    override fun hashCode(): Int {
        var result = bluetoothMac.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + inRange.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bluetoothMac)
        parcel.writeString(name)
        parcel.writeByte(if (inRange) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Device> {
        override fun createFromParcel(parcel: Parcel): Device {
            return Device(parcel)
        }

        override fun newArray(size: Int): Array<Device?> {
            return arrayOfNulls(size)
        }
    }
}
