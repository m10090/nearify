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
        parentColumns = ["bluetooth_mac"],
        childColumns = ["device_id"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class Action(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="action_id") val actionId: Int = 0,
    @ColumnInfo(name = "device_id", index = true) val macAddress: String,
    @ColumnInfo(name = "action_leave") val onLeave: Boolean = false,
    @ColumnInfo(name = "action_sight") val onSight: Boolean = false,
    @ColumnInfo(name = "message") val message: String = "",
    @Embedded val device: Device? = null,
)

