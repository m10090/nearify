package com.example.nearify.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.nearify.data.model.*
import java.time.LocalDateTime

@Dao
interface ActionDao {
    @Insert
    fun addAction(action: Action)

    @get:Query("SELECT * FROM `action`")
    val getAllActions: List<ActionWithDevice>


    @Query(
        """
        SELECT * FROM `action`
        WHERE device_id = :bluetoothMac
        """
    )
    fun getActions(bluetoothMac: String): List<Action>

    @Query("SELECT * FROM `action` WHERE device_id = :bluetoothMac AND action_leave ")
    fun getLeaveActions(bluetoothMac: String): List<Action>
    @Query("SELECT * FROM `action` WHERE device_id = :bluetoothMac AND action_sight ")
    fun getEnterActions(bluetoothMac: String): List<Action>

    @Delete
    fun deleteAction(action: Action)


    @Query(
        """
        DELETE FROM `action`
        where device_id = :deviceId
    """
    )
    fun deleteActions(deviceId: String)



    @Query("DELETE FROM `action` where device_id = :deviceId")
    fun deleteActions(deviceId: Int)

    @Delete
    fun deleteActions(vararg actions: Action)

    @Update
    fun updateAction(action: Action)

    @Update
    fun updateActions(vararg actions: Action)

    @Query("SELECT count(*) FROM `action` WHERE device_id = :bluetoothMac")
    fun getActionCount(bluetoothMac: String): Int

}