package com.example.nearify.ui.view

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.compose.ui.platform.ComposeView
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nearify.ui.view.composables.DeviceCard
import com.example.nearify.R
import com.example.nearify.data.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddDeviceList : AppCompatActivity() {
    private val devices: MutableSet<Device> = mutableSetOf()
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private lateinit var listview: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_device_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            return
        }
        val bluetoothAdapter = bluetoothAdapter!!
        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is not enabled, prompt user to enable it
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            startActivityForResult(enableBtIntent, 1)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }


        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }

        listview = findViewById(R.id.bluetooth_list)


        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                if (BluetoothDevice.ACTION_FOUND == action) {

                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return

                    val deviceName = if (ActivityCompat.checkSelfPermission(
                            this@AddDeviceList,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        "UnKnown"
                    } else
                        device.name ?: "UnKnown"
                    val deviceAddress = device.address
                    // add item to device name to listview
                    val deviceObj = Device(deviceAddress, deviceName)
                    GlobalScope.launch(Dispatchers.Main) {
                        devices.add(deviceObj)
                        listview.addView(ComposeView(this@AddDeviceList).apply {
                            setContent {
                                DeviceCard(deviceObj)
                            }
                        })
                    }
                }
            }
        }

        registerReceiver(receiver, filter)
        bluetoothAdapter.startDiscovery()
    }


}