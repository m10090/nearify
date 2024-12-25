package com.example.nearify.ui.view.composables

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearify.data.model.Device
import com.example.nearify.db
import com.example.nearify.ui.theme.purpleGradient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun SavedDeviceCard(device: Device) {
    var delete  = remember { mutableStateOf(false) }
    val buttonColor = Color(0xFFCF71F1)
    if (delete.value) return
    // Define a fixed size for the square card
    val cardSize = 150.dp
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(cardSize), // Make the card square
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(purpleGradient)
                .fillMaxSize(), // Fill the square card
            horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            Text(
                text = device.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp) // Add some space below the text
            )

            Text(
                text = device.bluetoothMac,
                color = Color.White,
                fontWeight = FontWeight.Normal, // No bold
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp) // Add some space below the text
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Handle add action */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Add")
                }

                // Notification count next to the button

                Button(
                    onClick = {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Are you sure?")
                            .setCancelable(false)
                            .setPositiveButton("Yes") { dialog, id ->
                                // Action when "Yes" is clicked
                                GlobalScope.launch(Dispatchers.IO) {
                                    db.deviceDao().deleteDevice(device)
                                }
                                delete.value = true
                            }
                            .setNegativeButton("No") { dialog, id ->
                                // Action when "No" is clicked
                            }
                        val alert = builder.create()
                        alert.show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                    // Add space between button and count
                )
                {
                    Text(text = "Remove")
                }

            }
        }
    }
}