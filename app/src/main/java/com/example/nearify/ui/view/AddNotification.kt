package com.example.nearify.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.nearify.MainActivity
import com.example.nearify.data.local.AppDatabase
import com.example.nearify.ui.theme.NearifyTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nearify.data.model.Action
import com.example.nearify.data.model.Device


private lateinit var db: AppDatabase
private var device = Device("AA:BB:CC:DD:EE:DD", "Cgmoreda")

class AddNotification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = MainActivity.db
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

@Composable
private fun MainScreen(
    modifier: Modifier,
    finish: () -> Unit /* so bad don't to this at home */ = {}
) {
    var enteredMessage by remember { mutableStateOf("Text") }

    var enteredOnLeave by remember { mutableStateOf(false) }
    var enteredOnSight by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val spacer = @Composable {
        Spacer(modifier = modifier.padding(top = 16.dp))
    }
    Column(modifier = modifier.padding(16.dp)) {
        Text(device.name, color = MaterialTheme.colorScheme.secondary)
        spacer()
        Text(
            "Notification Message",
            color = MaterialTheme.colorScheme.primary
        )
        TextField(
            value = enteredMessage,
            onValueChange = {
                enteredMessage = it
            },
            textStyle = MaterialTheme.typography.bodyLarge

        )
        spacer()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("on leave", color = MaterialTheme.colorScheme.primary)
            Checkbox(
                checked = enteredOnLeave,
                onCheckedChange = {
                    enteredOnLeave = it
                }
            )
        }
        spacer()
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "on sight", color = MaterialTheme.colorScheme.primary
            )
            Checkbox(
                checked = enteredOnSight,
                modifier = modifier,
                onCheckedChange = {
                    enteredOnSight = it
                }
            )
        }
        Button(onClick = {
            if (enteredOnLeave || enteredOnSight == false) {
                Toast.makeText(
                    context,
                    "Please select at least one option (on sight or on leave) ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            db.actionDao().addAction(
                Action(
                    message = enteredMessage,
                    onLeave = enteredOnLeave,
                    onSight = enteredOnSight,
                    macAddress = device.bluetoothMac
                )
            )
            finish()
        }) {
            Text("Add Notification")
        }
    }
}

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