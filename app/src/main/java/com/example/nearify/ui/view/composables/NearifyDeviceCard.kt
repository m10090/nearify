package com.example.nearify.ui.view.composables

import android.content.Intent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearify.data.model.Device
import com.example.nearify.ui.theme.purpleGradient
import com.example.nearify.ui.view.ActionList

@Composable
fun NearifyDeviceCard(cardData: Device, notificationCount: Int) {

    val context = LocalContext.current
    val buttonColor = Color(0xFFCF71F1)

    // Define a fixed size for the square card
    val cardSize = 150.dp

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
                text = cardData.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp) // Add some space below the text
            )

            Text(
                text = cardData.bluetoothMac,
                color = Color.White,
                fontWeight = FontWeight.Normal, // No bold
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp) // Add some space below the text
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, ActionList::class.java)
                        intent.putExtra("macAddress", cardData.bluetoothMac)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.width(110.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Nearifies")
                }

                // Notification count next to the button
                if (notificationCount > 0) {
                    Text(
                        text = notificationCount.toString(),
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp) // Add space between button and count
                    )
                }
            }
        }
    }
}