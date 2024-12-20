// SavedDevicesActivity.kt
package com.example.nearify.ui.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearify.R
import com.example.nearify.adapther.DeviceAdapter
import com.example.nearify.data.model.savedDevice
import com.example.nearify.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedDevicesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private val deviceList = mutableListOf<savedDevice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_devices)

        recyclerView = findViewById(R.id.recyclerViewDevices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        deviceAdapter = DeviceAdapter(deviceList, { device ->
            deleteDevice(device)
        })
        recyclerView.adapter = deviceAdapter
        loadSavedDevices()
    }

    private fun loadSavedDevices() {

        deviceAdapter = DeviceAdapter(mutableListOf(), { device ->
            deleteDevice(device)
        })
        GlobalScope.launch {
            var devices = db.deviceDao().getAllDevices
            for (d in devices){
                val dd = savedDevice(name = d.name , macAddress = d.bluetoothMac)
                deviceAdapter.addDevice(dd)
            }
            withContext(Dispatchers.Main){
                recyclerView.adapter = deviceAdapter
            }
        }
    }

    private fun deleteDevice(device: savedDevice) {
        GlobalScope.launch(Dispatchers.IO) {
            db.deviceDao().deleteDevice(device.macAddress)
            withContext(Dispatchers.Main){
                loadSavedDevices()
            }
        }
    }
}


