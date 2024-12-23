package com.example.nearify

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.nearify.data.local.AppDatabase
import com.example.nearify.data.model.Device
import com.example.nearify.ui.theme.NearifyTheme
import com.example.nearify.ui.view.ActionNotification
import com.example.nearify.ui.view.AddDeviceList
import com.example.nearify.ui.view.SavedDevicesActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


val db: AppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    _db_intial!!
}
var _db_intial: AppDatabase? = null
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //val intent = Intent(this, Perms::class.java)
        //startActivity(intent)

        setContentView(R.layout.activity_saved_devices)

        enableEdgeToEdge()
        GlobalScope.launch(Dispatchers.IO) {

            if(_db_intial != null) {
                return@launch
            }
            scheduleJob()
            _db_intial =
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "nearify-db")
                    .build()

            if (db.deviceDao().getAllDevices.isEmpty()) {
                db.deviceDao().insert(Device("AA:BB:CC:DD:EE:FF", "Cgmoreda"))
            }

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
    private fun scheduleJob() {
        // Get the JobScheduler system service
        val serviceIntent = Intent(
            this,
            NearifyService::class.java
        )
        startService(serviceIntent)



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