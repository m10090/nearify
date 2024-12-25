package com.example.nearify.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.nearify.R
import com.example.nearify.data.model.Device
import com.example.nearify.db
import com.example.nearify.ui.view.composables.SavedDeviceCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedDeviceList : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.composite_container, container, false)

        val context = requireContext()
        GlobalScope.launch(Dispatchers.IO) {
            devices = db.deviceDao().getAllDevices


            withContext(Dispatchers.Main) {
                val container =  root.findViewById<LinearLayout>(R.id.compose_container)
                val content = ComposeView(context).apply {
                    setContent {
                        MainScreen()
                    }
                }
                container.addView(content)
            }

        }
        return root
    }
}
@Composable
private fun MainScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp) // Optional for more spacing around the grid
    ) {
        items(devices.size) {
            SavedDeviceCard(devices[it])
        }
    }
}
