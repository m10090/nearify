package com.example.nearify.data.local

import android.content.Context
import com.example.nearify.data.dao.*
import com.example.nearify.data.model.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Device::class, Action::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun actionDao(): ActionDao

}