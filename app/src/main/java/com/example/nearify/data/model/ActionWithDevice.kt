package com.example.nearify.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ActionWithDevice(
    @Embedded val action: Action,
    @Relation(
        parentColumn = "device_id",
        entityColumn = "id"
    )
    val device: Device
)
data class DeviceWithAction (
    @Embedded val device: Device,
    @Relation(
        parentColumn = "id",
        entityColumn = "device_id"
    )
    val actions: List<Action>
)
