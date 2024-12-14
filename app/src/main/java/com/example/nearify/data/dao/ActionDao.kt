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

    @get:Query("SELECT * FROM action where until_datetime < :now")
    val getAllActions: List<Action>

    // delete action
    @Query("DELETE FROM action WHERE id = :id")
    fun deleteAction(actionId: Int)

    @Query(
        """
        SELECT * FROM action 
        WHERE device_id = :deviceId AND action_type = :onLeave
        AND until_datetime < :now"""
    )
    fun getActions(deviceId: Int, onLeave: Boolean): List<Action>

    @Delete
    fun deleteAction(action: Action)


    @Query(
        """
        DELETE FROM action 
        where device_id = :deviceId AND action_type = :onLeave
    """
    )
    fun deleteActions(deviceId: Int, onLeave: Boolean)


    @Query("DELETE FROM action where device_id = :deviceId")
    fun deleteActions(deviceId: Int)

    @Delete
    fun deleteActions(vararg actions: Action)

    @Update
    fun updateAction(action: Action)

    @Update
    fun updateActions(vararg actions: Action)

}