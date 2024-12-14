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
