package com.example.nearify.ui.view


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nearify.R
import com.example.nearify.data.model.Action
import com.example.nearify.data.model.ActionWithDevice
import com.example.nearify.db
import com.example.nearify.ui.view.composables.ActionCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private lateinit var items: List<Action>
class ActionList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val macAddres = intent.getStringExtra("macAddress") ?: ""
        GlobalScope.launch(Dispatchers.IO) {

            items = db.actionDao().getActions(macAddres)
            withContext(Dispatchers.Main) {
                setContent {
                    Column(
                        modifier = Modifier
                            .padding(WindowInsets.statusBars.asPaddingValues()) .padding(5.dp)// Adjust based on system bars
                    ) {
                        MainScreen()
                    }
                }

            }
        }
        setContent{

            MainScreen()
        }
    }
}
@Composable
private fun MainScreen(

) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize() // Ensures it takes up the available space
            .padding(8.dp) // Adds padding for the scrollable area
    ) {
        items(items) { item ->
            ActionCard(
                message = item.message,
                isOnSight = item.onSight,
                isOnLeave = item.onLeave,
                id = item.actionId
            )
        }
    }
}

