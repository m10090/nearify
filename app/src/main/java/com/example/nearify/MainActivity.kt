package com.example.nearify

import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.nearify.data.local.AppDatabase
import com.example.nearify.data.model.Device
import com.example.nearify.ui.theme.NearifyTheme
import com.example.nearify.ui.view.ActionNotification
import com.example.nearify.ui.view.AddDeviceList
import com.example.nearify.ui.view.SavedDevicesActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


val db: AppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    _db_intial
}
lateinit var _db_intial: AppDatabase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_devices)



        enableEdgeToEdge()
        GlobalScope.launch(Dispatchers.IO) {

            _db_intial =
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "nearify-db")
                    .build()

            if (db.deviceDao().getAllDevices.isEmpty()) {
                db.deviceDao().insert(Device("AA:BB:CC:DD:EE:FF", "Cgmoreda"))
            }

        }
        GlobalScope.launch {
            connect()
        }
        setContent {
            NearifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize())
                { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Suppress("DEPRECATION", "MissingPermission")
    private suspend fun connect() {
        delay(2000)
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, 1)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        val pairedDevices = bluetoothAdapter.bondedDevices
        val devices = db.deviceDao().getAllDevices
        val to_delete = mutableSetOf<String>()
        devices.forEach { device ->
            if(pairedDevices.none {
                it.address == device.bluetoothMac
            }){
                to_delete.add(device.bluetoothMac)
            }
        }
        db.deviceDao().deleteDevices(to_delete)
        val newInRange = mutableSetOf<String>()
        for (device in pairedDevices) {
            val deviceAddress = device.address
            if (devices.none {
                    it.bluetoothMac == deviceAddress
                }) continue
            // Use deviceName and deviceAddress to identify the paired device
            lateinit var bluetoothSocket: BluetoothSocket
            val uuid = device.uuids[0].uuid // UUID of the device’s service

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                bluetoothSocket.connect()
                // Now you are connected, you can start communicating with the device
                newInRange.add(deviceAddress)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val newOutOfRange = setOf(*devices.map {
            it.bluetoothMac
        }.toTypedArray()) - newInRange;
        val oldInRange = db.deviceDao().getInRangeDevices.map { it.device.bluetoothMac }
        val oldOutOfRange = db.deviceDao().getOutOfRangeDevices.map { it.device.bluetoothMac }
        val notifications = mutableListOf<String>()
        oldInRange.forEach { device ->
            if (newOutOfRange.contains(device)) {
                db.deviceDao().updateDevice(device, false)
                notifications.addAll(db.actionDao().getLeaveActions(device).map {
                    it.message
                })
            }
        }
        oldOutOfRange.forEach { device ->
            if(newInRange.contains(device)) {
                db.deviceDao().updateDevice(device, true)
                notifications.addAll(db.actionDao().getEnterActions(device).map {
                    it.message
                })
            }
        }
        val channelId = "notification_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon) // Use an appropriate notification icon
            .setContentTitle("Somebody is here or isn't")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Assuming 'notification' is a list of messages
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifications.forEachIndexed { index, message ->
            val notification = notificationBuilder.setContentText(message)
            val notificationId = index + 1
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}

@Composable
private fun MainScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ButtonsColumn()
    }
}

@Preview
@Composable
fun previewMainScreen() {
    NearifyTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            MainScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun ButtonsColumn() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(vertical = 200.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoToNotification()
        Spacer(modifier = Modifier.height(16.dp)) // Space between buttons
        GoToAddDevicesList()
        Spacer(modifier = Modifier.height(16.dp)) // Space between buttons
        GoToSavedDevicesList()
    }
}


@Composable
private fun GoToNotification() {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Added horizontal padding
        onClick = {
            val intent = Intent(context, ActionNotification::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF87CEEB)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Go To Notification",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GoToAddDevicesList() {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Added horizontal padding
        onClick = {
            val intent = Intent(context, AddDeviceList::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF87CEEB)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DevicesOther,
                contentDescription = "Device Icon",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Go To Device",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GoToSavedDevicesList() {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Added horizontal padding
        onClick = {
            val intent = Intent(context, SavedDevicesActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF87CEEB)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Saved Devices Icon",
                modifier = Modifier
                    .size(50.dp)

            )
            Text(
                text = "View Saved Devices",
                textAlign = TextAlign.Center
            )
        }
    }
}