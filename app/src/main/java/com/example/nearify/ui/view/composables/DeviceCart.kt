package com.example.nearify.ui.view.composables

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.nearify.data.model.Device
import com.example.nearify.ui.view.AddDevice


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeviceCard(deviceModel: Device) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Column {
                Row {
                    Text(
                        text = deviceModel.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Center Column
                FlowColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = deviceModel.bluetoothMac,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(onClick = {
                        val intent = Intent(context, AddDevice::class.java)
                        intent.putExtra("device", deviceModel)
                        context.startActivity(intent)
                    }) {
                        Text(text = "Add")
                    }
                }
            }
        }
    }
}