package com.example.nearify

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.nearify.data.local.AppDatabase
import com.example.nearify.data.model.Device
import com.example.nearify.ui.theme.NearifyTheme
import com.example.nearify.ui.view.ActionNotification
import com.example.nearify.ui.view.DeviceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        GlobalScope.launch(Dispatchers.IO) {

            db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "nearify-db")
                .build()

            if (db.deviceDao().getAllDevice.isEmpty()) {
                db.deviceDao().insert(Device("AA:BB:CC:DD:EE:DD", "Cgmoreda"))
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
        val intent = Intent(context, DeviceList::class.java)
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NearifyTheme {

    }
}
