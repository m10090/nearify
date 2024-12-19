package com.example.nearify.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import com.example.nearify.data.model.Device
import com.example.nearify.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddedDevicesList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var devices: List<Device>
        GlobalScope.launch(Dispatchers.IO) {
            val _devices = db.deviceDao().getAllDevices
            GlobalScope.launch(Dispatchers.Main) {
                devices = _devices
            }
        }


    }
}

@Composable
private fun MainScreen(modifier: Modifier, devices: List<Device>) {
    val context = LocalContext.current
    LazyColumn(modifier) {
        item {
            HeaderRow(modifier)
        }
        items(devices) {
            Row(modifier,
                verticalAlignment = Alignment.CenterVertically) {
                Text(it.name)
                Text(it.bluetoothMac)
                Button(
                    onClick = {
                        val intent = Intent(context, AddNotification::class.java)
                        intent.putExtra("device", it)
                        context.startActivity(intent)
                    }
                ) {
                    Text("add Notification")
                }
                Button(onClick =
                {
                    GlobalScope.launch(Dispatchers.IO) {
                        db.deviceDao().deleteDevice(it)
                        // remove this
                        // todo:
                    }
                }) { }

            }
        }
    }
}


@Composable
private fun HeaderRow(modifier: Modifier) {
    Row(
        modifier
            .background(color = Color.Gray),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Device Name",modifier=modifier.weight(0.7f))
        Text("Device Mac Address",modifier=modifier.weight(0.7f))
        Text("Add Notification",modifier=modifier.weight(0.7f))
        Text("Remove Device",modifier=modifier.weight(0.7f))
    }
}