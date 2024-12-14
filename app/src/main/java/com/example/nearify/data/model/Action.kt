package com.example.nearify.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "action",
    foreignKeys = [ForeignKey(
        entity = Device::class,
        parentColumns = ["id"],
        childColumns = ["device_id"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class Action(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "device_id", index = true) val deviceId: Int,
    @ColumnInfo(name = "action_type") val onLeave: Boolean,
    @ColumnInfo(name = "until_datetime") val untilDatetime: LocalDateTime = LocalDateTime.MAX,
    @ColumnInfo(name = "message") val message: String = "",
    @Embedded val device: Device
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Action

        if (id != other.id) return false
        if (deviceId != other.deviceId) return false
        if (onLeave != other.onLeave) return false
        if (untilDatetime != other.untilDatetime) return false
        if (message != other.message) return false
        if (device != other.device) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + deviceId
        result = 31 * result + onLeave.hashCode()
        result = 31 * result + untilDatetime.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + device.hashCode()
        return result
    }
}
