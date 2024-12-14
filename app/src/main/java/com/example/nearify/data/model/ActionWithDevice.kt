package com.example.nearify.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ActionWithDevice(
    @Embedded val action: Action,
    @Relation(
        parentColumn = "device_id",
        entityColumn = "bluetooth_mac"
    )
    val device: Device
)
data class DeviceWithAction (
    @Embedded val device: Device,
    @Relation(
        parentColumn = "bluetooth_mac",
        entityColumn = "device_id"
    )
    val actions: List<Action>
)
