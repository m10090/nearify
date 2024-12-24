package com.example.nearify.ui.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearify.MainActivity
import com.example.nearify.NotificationsFragment
import com.example.nearify.data.model.Device
import com.example.nearify.db
import com.example.nearify.ui. view.pairDeviceByMacAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun DeviceCard(device: Device) {
    // State to track whether the card is expanded
    var isExpanded by remember { mutableStateOf(false) }
    var deviceName by remember { mutableStateOf("") }
    var done by remember { mutableStateOf(false) }
    if (done) {
        return
    }
    val context = LocalContext.current


    val buttonColor = Color(0xFF8E24AA)

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(Modifier.background(com.example.nearify.ui.theme.purpleGradient)) { // Assuming 'purple' is a color value
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) { // Takes up remaining horizontal space
                    Text(
                        text = device.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = device.bluetoothMac,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Add the button to the right
                Button(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.padding(start = 8.dp).width(80.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor, // Set container color
                        contentColor = Color.White  // Set text color to white for contrast
                    )
                ) {
                    Text(text = "Add")
                }
            }

            // Conditionally show the text field and "Pair" button if expanded
            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    TextField(
                        value = deviceName,
                        onValueChange = { deviceName = it },
                        label = { Text("Device Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            pairDeviceByMacAddress(device.bluetoothMac, context)
                            GlobalScope.launch(Dispatchers.IO) {
                                db.deviceDao().insert(device)
                            }
                            done = true
                                  },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor, // Set container color
                            contentColor = Color.White  // Set text color to white for contrast
                        )
                    ) {
                        Text(text = "Pair")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCardUi() {
    // Example data to display in the preview
    val exampleCardData = Device(
        name = "Bluetooth Speaker",
        bluetoothMac = "00:1A:7D:DA:71:13",
    )
    DeviceCard(device = exampleCardData)
}