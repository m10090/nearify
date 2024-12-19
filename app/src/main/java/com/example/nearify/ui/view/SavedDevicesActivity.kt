// SavedDevicesActivity.kt
package com.example.nearify.ui.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearify.R
import com.example.nearify.adapther.DeviceAdapter
import com.example.nearify.data.model.savedDevice
//
//class SavedDevicesActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var deviceAdapter: DeviceAdapter
//    private val deviceList = mutableListOf<savedDevice>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_saved_devices)
//
//        recyclerView = findViewById(R.id.recyclerViewDevices)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        deviceAdapter = DeviceAdapter(deviceList) { device ->
//            deleteDevice(device)
//        }
//        recyclerView.adapter = deviceAdapter
//
//        findViewById<Button>(R.id.btnSavedDevices).setOnClickListener {
//            loadSavedDevices()
//        }
//    }
//
//    private fun loadSavedDevices() {
//        // Fetch devices from the database and update the deviceList
//        // For example:
//        // deviceList.clear()
//        // deviceList.addAll(database.getAllDevices())
//        // deviceAdapter.notifyDataSetChanged()
//    }
//
//    private fun deleteDevice(device: savedDevice) {
//        // Remove the device from the database
//        // For example:
//        // database.deleteDevice(device)
//        // deviceList.remove(device)
//        // deviceAdapter.notifyDataSetChanged()
//    }
//}



class SavedDevicesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_devices)

        recyclerView = findViewById(R.id.recyclerViewDevices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create a list of fake devices
        val fakeDevices = listOf(
            savedDevice(name = "Device 1", macAddress = "00:11:22:33:44:55"),
            savedDevice(name = "Device 2", macAddress = "66:77:88:99:AA:BB"),
            savedDevice(name = "Device 3", macAddress = "CC:DD:EE:FF:00:11")
        )

        // Initialize the adapter with the fake data
        deviceAdapter = DeviceAdapter(fakeDevices) { device ->
            // Handle delete action (for now, just print the device name)
            println("Delete clicked for: ${device.name}")
        }
        recyclerView.adapter = deviceAdapter
    }
}