package com.example.nearify.ui.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.ui.platform.ComposeView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.nearify.ui.view.composables.DeviceCard
import com.example.nearify.R
import com.example.nearify.data.model.Device
import com.example.nearify.db
import com.google.android.flexbox.FlexboxLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddDeviceList : Fragment() {
    private val devices: MutableSet<Device> = mutableSetOf()
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var savedDevices : Set<String>? = null
    private lateinit var listview: LinearLayout
    private lateinit var context : Context

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext()

        val root = inflater.inflate(R.layout.activity_device_list,container,false)

        ViewCompat.setOnApplyWindowInsetsListener(root.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if ( savedDevices == null) {
            GlobalScope.launch(Dispatchers.IO) {
                val set = setOf(*db.deviceDao().getAllDevices.map {
                    it.bluetoothMac
                }.toTypedArray())
                withContext(Dispatchers.Main) {
                    savedDevices = set
                }

            }
        }

        val bluetoothAdapter = bluetoothAdapter!!
        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is not enabled, prompt user to enable it
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }



        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }

        listview = root.findViewById(R.id.bluetooth_list)


        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val receiver = object : BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action


                if (BluetoothDevice.ACTION_FOUND == action) {
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return

                    val deviceName =
                        device.name ?: "Unknown Device" // get device name
                    val deviceAddress = device.address
                    // add item to device name to listview
                    val deviceObj = Device(deviceAddress, deviceName)


                    //// iam here
                    GlobalScope.launch(Dispatchers.Main) {
                        if(deviceObj.bluetoothMac !in savedDevices!!){
                            devices.add(deviceObj)
                        }
                        else return@launch

                        listview.addView(ComposeView(context!!).apply {
                            setContent {
                                DeviceCard(deviceObj)
                            }
                        })
                    }


                }
            }
        }
        context.registerReceiver(receiver, filter)
        bluetoothAdapter.startDiscovery()
        return root
    }
    @Composable
    private fun MainScreen(){
        LazyColumn(modifier = Modifier.fillMaxHeight().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(listOf<Device>()){
                     DeviceCard(device = it)
            }
        }
    }


}

