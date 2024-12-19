package com.example.nearify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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




val db : AppDatabase by lazy {
    _db_intial
}
lateinit var _db_intial: AppDatabase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_devices)



        enableEdgeToEdge()
        GlobalScope.launch(Dispatchers.IO) {

            _db_intial = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "nearify-db")
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

        val btnViewSavedDevices: Button = findViewById(R.id.btnSavedDevices)
        btnViewSavedDevices.setOnClickListener {
            val intent = Intent(this, SavedDevicesActivity::class.java)
            startActivity(intent)
        }

    }

}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier)
        GoToNotification()
        Spacer(modifier = Modifier)
        GoToAddDevicesList()
    }
}

@Composable
fun GoToAddDevicesList() {
    val context = LocalContext.current
    Button(onClick = {
        val intent = Intent(context, AddDeviceList::class.java)
        context.startActivity(intent)
    }) {
        Text(text = "Go To Device")
    }
}

@Composable
fun GoToNotification() {
    val context = LocalContext.current
    Button(onClick = {
        val intent = Intent(context, ActionNotification::class.java)
        context.startActivity(intent)
    }) {
        Text(text = "Go To Notification")
    }
}




