// DeviceAdapter.kt
package com.yourapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nearify.R
import com.example.nearify.data.model.savedDevice

class DeviceAdapter(
    private val devices: List<savedDevice>,
    private val onDeleteClick: (savedDevice) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDeviceName: TextView = itemView.findViewById(R.id.tvDeviceName)
        val tvDeviceMac: TextView = itemView.findViewById(R.id.tvDeviceMac)
        val btnDeleteDevice: Button = itemView.findViewById(R.id.btnDeleteDevice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.tvDeviceName.text = device.name
        holder.tvDeviceMac.text = device.macAddress
        holder.btnDeleteDevice.setOnClickListener { onDeleteClick(device) }
    }

    override fun getItemCount(): Int = devices.size
}