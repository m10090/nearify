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
    @PrimaryKey(autoGenerate = true) val actionId: Int = 0,
    @ColumnInfo(name = "device_id", index = true) val deviceId: Int,
    @ColumnInfo(name = "action_leave") val onLeave: Boolean = false,
    @ColumnInfo(name = "action_sight") val onSight: Boolean = false,
    @ColumnInfo(name = "message") val message: String = "",
    @Embedded val device: Device? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Action

        if (actionId != other.actionId) return false
        if (deviceId != other.deviceId) return false
        if (onLeave != other.onLeave) return false
        if (message != other.message) return false
        if (device != other.device) return false

        return true
    }

    override fun hashCode(): Int {
        var result = actionId
        result = 31 * result + deviceId
        result = 31 * result + onLeave.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + device.hashCode()
        return result
    }
}
