// SavedDevicesActivity.kt
package com.example.nearify.ui.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearify.R
import com.yourapp.adapter.DeviceAdapter
import com.example.nearify.data.model.savedDevice

class SavedDevicesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private val deviceList = mutableListOf<savedDevice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_devices)

        recyclerView = findViewById(R.id.recyclerViewDevices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        deviceAdapter = DeviceAdapter(deviceList) { device ->
            deleteDevice(device)
        }
        recyclerView.adapter = deviceAdapter

        findViewById<Button>(R.id.btnSavedDevices).setOnClickListener {
            loadSavedDevices()
        }
    }

    private fun loadSavedDevices() {
        // Fetch devices from the database and update the deviceList
        // For example:
        // deviceList.clear()
        // deviceList.addAll(database.getAllDevices())
        // deviceAdapter.notifyDataSetChanged()
    }

    private fun deleteDevice(device: savedDevice) {
        // Remove the device from the database
        // For example:
        // database.deleteDevice(device)
        // deviceList.remove(device)
        // deviceAdapter.notifyDataSetChanged()
    }
}