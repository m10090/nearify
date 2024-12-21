package com.example.nearify.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.nearify.ui.theme.NearifyTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nearify.data.model.Action
import com.example.nearify.data.model.Device
import com.example.nearify.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch





private var device = Device("AA:BB:CC:DD:EE:DD", "Cgmoreda")
class AddNotification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        device = intent.getParcelableExtra("device") ?: device
        setContent {
            NearifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize())
                { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding)) {
                        finish()
                    }
                }
            }
        }
    }
}

//@Composable
//private fun MainScreen(
//    modifier: Modifier,
//    finish: () -> Unit /* so bad don't to this at home */ = {}
//) {
//
//    var enteredMessage by remember { mutableStateOf("Text") }
//    var enteredOnLeave by remember { mutableStateOf(false) }
//    var enteredOnSight by remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//    val spacer = @Composable {
//        Spacer(modifier = modifier.padding(top = 16.dp))
//    }
//    Column(modifier = modifier.padding(16.dp)) {
//        Text(device.name, color = MaterialTheme.colorScheme.secondary)
//        spacer()
//        Text(
//            "Notification Message",
//            color = MaterialTheme.colorScheme.primary
//        )
//        TextField(
//            value = enteredMessage,
//            onValueChange = {
//                enteredMessage = it
//            },
//            textStyle = MaterialTheme.typography.bodyLarge
//
//        )
//
//
//
//        spacer()
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text("on leave", color = MaterialTheme.colorScheme.primary)
//            Checkbox(
//                checked = enteredOnLeave,
//                onCheckedChange = {
//                    enteredOnLeave = it
//                }
//            )
//        }
//        spacer()
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                "on sight", color = MaterialTheme.colorScheme.primary
//            )
//            Checkbox(
//                checked = enteredOnSight,
//                modifier = modifier,
//                onCheckedChange = {
//                    enteredOnSight = it
//                }
//            )
//        }
//        Button(onClick = {
//
//            GlobalScope.launch(Dispatchers.IO) {
//                if (!(enteredOnLeave || enteredOnSight)) {
//                    Toast.makeText(
//                        context,
//                        "Please select at least one option (on sight or on leave)",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                db.actionDao().addAction(
//                    Action(
//                        message = enteredMessage,
//                        onLeave = enteredOnLeave,
//                        onSight = enteredOnSight,
//                        macAddress = device.bluetoothMac
//                    )
//                )
//            }
//
//            finish()
//        }) {
//            Text("Add Notification")
//        }
//    }
//}




@Preview
@Composable
private fun PreviewMainScreen() {
    NearifyTheme {
        Scaffold(modifier = Modifier.fillMaxSize())
        { innerPadding ->
            MainScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun MainScreen(
    modifier: Modifier,
    finish: () -> Unit = {}
) {
    var enteredMessage by remember { mutableStateOf("") }
    var enteredOnLeave by remember { mutableStateOf(false) }
    var enteredOnSight by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Device Name Card
        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Device",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Message Input
        Text(
            text = "Notification Message",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        TextField(
            value = enteredMessage,
            onValueChange = { enteredMessage = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)  // Increased height
                .padding(bottom = 24.dp),
            placeholder = { Text("Enter your notification message") },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent
            ),
            minLines = 4,  // Minimum number of lines
            maxLines = 6,  // Maximum number of lines
            textStyle = MaterialTheme.typography.bodyLarge
        )

        // Notification Options
        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Notification Triggers",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Checkbox(
                        checked = enteredOnLeave,
                        onCheckedChange = { enteredOnLeave = it },
                        colors = androidx.compose.material3.CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "Notify on Leave",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = enteredOnSight,
                        onCheckedChange = { enteredOnSight = it },
                        colors = androidx.compose.material3.CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "Notify on Sight",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Add Button
        Spacer(modifier = Modifier.weight(1f)) // This pushes the button to the bottom

        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    if (!(enteredOnLeave || enteredOnSight)) {
                        Toast.makeText(
                            context,
                            "Please select at least one option (on sight or on leave)",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }
                    db.actionDao().addAction(
                        Action(
                            message = enteredMessage,
                            onLeave = enteredOnLeave,
                            onSight = enteredOnSight,
                            macAddress = device.bluetoothMac
                        )
                    )
                }
                finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Add padding from bottom of screen
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                "Add Notification",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

