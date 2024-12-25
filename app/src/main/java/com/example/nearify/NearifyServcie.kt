package com.example.nearify

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

private val TAG = "NotifService"

class NearifyService : Service() {

    private val handler = Handler()
    val context = this
    override fun onCreate() {
        super.onCreate()
        Log.d("PeriodicService", "Service created")

        // Define the runnable tas

        // Start the first task
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PeriodicService", "Service destroyed")
        // Stop the task when service is destroyed
        handler.removeCallbacks(runnable)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    private val runnable: Runnable = object : Runnable {

        override fun run() {
            val channelId = "notification_channel"
            val notificationManager =
                context.getSystemService("notification") as NotificationManager


            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification_icon) // Ensure this drawable exists
                .setContentTitle("Device Status Update")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(longArrayOf(0, 250, 250, 250))
                .setAutoCancel(true)
            val name = "My Notification Channel"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
            GlobalScope.launch(Dispatchers.IO) {
                Log.i("NearifyWorker", "searching for devices")
                // Request notification permission if targeting Android 13 or higher


                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


                val pairedDevices = bluetoothAdapter.bondedDevices
                val devices = db.deviceDao().getAllDevices
                val toDelete = mutableSetOf<String>()
                devices.forEach { device ->
                    if (pairedDevices.none { it.address == device.bluetoothMac }) {
                        toDelete.add(device.bluetoothMac)
                    }
                }
                db.deviceDao().deleteDevices(toDelete)
                val newInRange = mutableSetOf<String>()
                for (device in pairedDevices) {
                    val deviceAddress = device.address
                    if (devices.none { it.bluetoothMac == deviceAddress }) continue
                    val uuid = device.uuids[0].uuid // UUID of the device’s service

                    try {
                        val bluetoothSocket: BluetoothSocket =
                            device.createRfcommSocketToServiceRecord(uuid)
                        bluetoothSocket.connect()
                        // Now you are connected, you can start communicating with the device
                        newInRange.add(deviceAddress)
                        bluetoothSocket.close() // Close the socket when done
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                val newOutOfRange = devices.map { it.bluetoothMac }.toSet() - newInRange
                val oldInRange = db.deviceDao().getInRangeDevices.map { it.device.bluetoothMac }
                val oldOutOfRange =
                    db.deviceDao().getOutOfRangeDevices.map { it.device.bluetoothMac }
                val notifications = mutableListOf<String>()
                oldInRange.forEach { device ->
                    if (newOutOfRange.contains(device)) {
                        db.deviceDao().updateDevice(device, false)
                        notifications.addAll(
                            db.actionDao().getLeaveActions(device).map { it.message })
                    }
                }
                oldOutOfRange.forEach { device ->
                    if (newInRange.contains(device)) {
                        db.deviceDao().updateDevice(device, true)
                        notifications.addAll(
                            db.actionDao().getEnterActions(device).map { it.message })
                    }
                }


                notifications.forEachIndexed { index, message ->
                    val notification = notificationBuilder.setContentText(message).build()
                    val notificationId = index + 1
                    Log.i("NotifService","sending notfication")
                    withContext(Dispatchers.Main) {
                        notificationManager.notify(notificationId, notification)
                    }
                }
            }
            handler.postDelayed(this, 15000) // 15000ms = 15s
        }
    }
}



