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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.nearify.data.local.AppDatabase
import com.example.nearify.data.model.Action
import com.example.nearify.data.model.Device
import com.example.nearify.ui.theme.NearifyTheme
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

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        Column(modifier = modifier) {
            Greeting(name = "Android")
            Spacer(modifier = Modifier)
            GoToNotification()
        }
    }

    @Composable
    fun GoToNotification() {
        Button(onClick = {
            val intent = Intent(this, ActionNotification::class.java)
            startActivity(intent)
        }) {
            Text(text = "Go To Notification")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NearifyTheme {
        Greeting("Android")
    }
}
