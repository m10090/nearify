package com.example.nearify.ui.view

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nearify.MainActivity
import com.example.nearify.data.local.AppDatabase
import com.example.nearify.data.model.Device
import com.example.nearify.ui.theme.NearifyTheme
import com.example.nearify.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private var device = Device("AA:BB:CC:DD:EE:DD", "Cgmoreda")

class AddDevice : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

// val modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        @Suppress("DEPRECATION")
        device = intent.getParcelableExtra("device") ?: device
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize())
            { innerPadding ->
                MainScreen(modifier = Modifier.padding(innerPadding)){
                    this@AddDevice.finish()
                }
            }
        }
    }
}

@Composable
private fun MainScreen(modifier: Modifier = Modifier , finish:()->Unit ) {
// make a mutable string named bluetooth
    val enteredMacAddress = remember { mutableStateOf(device.bluetoothMac) } // basic React js
    val enteredDeviceName = remember { mutableStateOf(device.name) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize() // Ensures the Column fills the available space
            .padding(16.dp)
    ) {
        Text("Device Name")
        TextField(
            value = enteredDeviceName.value,
            onValueChange = {
                enteredDeviceName.value = it
                device = device.copy(name = it)
            },
        )
        Text("Device Mac Address")
        TextField(
            value = enteredMacAddress.value,
            onValueChange = {
                enteredMacAddress.value = it
            },
            modifier = Modifier.onFocusChanged { focusState ->
                if (!focusState.isFocused &&
                    enteredMacAddress.value != device.bluetoothMac
                ) {
                    if (validateMacAddress(enteredMacAddress.value, context)) {
                        device = device.copy(bluetoothMac = enteredMacAddress.value)
                    } else {
                        enteredMacAddress.value = device.bluetoothMac
                    }
                }
            }
        )
        Button(
            onClick = {
                pairDeviceByMacAddress(enteredMacAddress.value, context)
                GlobalScope.launch(Dispatchers.IO) {
                    db.deviceDao().insert(device)
                }
                finish()
            }
        ) {
            Text("Pair Device")
        }
    }
}

private fun validateMacAddress(macAddress: String, context: Context): Boolean {
    if (isMacAddressValid(macAddress)) {
        return true
    }
    Toast.makeText(context, "Invalid Mac Address", Toast.LENGTH_SHORT).show()
    return false
}

private fun isMacAddressValid(macAddress: String): Boolean {
    return macAddress.matches("[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}".toRegex())
}

@Preview
@Composable
private fun PreviewScreen() {
    NearifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            MainScreen(modifier = Modifier.padding(innerPadding)){} // This passes padding to MainScreen
        }
    }
}

// function to automatic pairing

fun pairDeviceByMacAddress(macAddress: String, context: Context) {
    Toast.makeText(context, macAddress, Toast.LENGTH_SHORT).show()
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    if (bluetoothAdapter == null) {
        Toast.makeText(context, "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT).show()
        return
    }

    if (!bluetoothAdapter.isEnabled) {
        Toast.makeText(context, "Please enable Bluetooth first.", Toast.LENGTH_SHORT).show()
        return
    }

    val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(macAddress)

    if (device == null) {
        Toast.makeText(context, "Device not found for this MAC address.", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        val createBondMethod = device.javaClass.getMethod("createBond")
        val isPaired = createBondMethod.invoke(device) as Boolean
        if (isPaired) {
            Toast.makeText(context, "Pairing initiated. Please confirm on the other device.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Pairing failed to start.", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error initiating pairing: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}