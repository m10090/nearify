package com.example.nearify.ui.view

import android.content.Context
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


private var device = Device("AA:BB:CC:DD:EE:DD", "Cgmoreda")
private lateinit var  db:AppDatabase

class AddDevice : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = MainActivity.db

//        val modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        @Suppress("DEPRECATION")
        device = intent.getParcelableExtra("device") ?: device
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize())
            { innerPadding ->
                MainScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}


@Composable
private fun MainScreen(modifier: Modifier = Modifier) {
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
        Button(onClick = {
            db.deviceDao().insert(device)
        }) {
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
            MainScreen(modifier = Modifier.padding(innerPadding)) // This passes padding to MainScreen
        }
    }
}
