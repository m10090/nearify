package com.example.nearify

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


import androidx.room.Room
import com.example.nearify.data.local.AppDatabase
import com.example.nearify.data.model.Action
import com.example.nearify.data.model.Device
import com.example.nearify.databinding.ActivityMainBinding
import com.example.nearify.ui.view.ActionNotification
import com.example.nearify.ui.view.AddDeviceList

import com.example.nearify.ui.view.AddDevuceFragment
import com.example.nearify.ui.view.InfoFragment

import com.example.nearify.ui.view.NearifyList
import com.example.nearify.ui.view.SavedDeviceList
import com.example.nearify.ui.view.composables.SavedDeviceCard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


val db: AppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    _db_intial!!
}


var _db_intial: AppDatabase? = null

class MainActivity : AppCompatActivity() {



    private lateinit var fragmentManager: FragmentManager
    companion object {
        lateinit var binding: ActivityMainBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!(Perms.permissionsToRequest.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            })) {
            val intent = Intent(this, Perms::class.java)
            startActivity(intent)
            return
        }
        // Set up the toolbar

        // Set up the bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {

//                R.id.saved_devices_btn -> openFragment(SavedDevicesFragment())
                R.id.add_device_btn -> openFragment(AddDeviceList())
                R.id.noti_btn -> openFragment(NearifyList())
                R.id.saved_devices_btn -> openFragment(SavedDeviceList())
                R.id.info_btn -> openFragment(InfoFragment())
            }
            true
        }


        fragmentManager = supportFragmentManager
        openFragment(NearifyList())
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivity(enableBtIntent)
        // scheduleJob()
        GlobalScope.launch(Dispatchers.IO) {

            if (_db_intial != null) {
                return@launch
            }

            _db_intial =
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "nearify-db")
                    .build()

            if (db.deviceDao().getAllDevices.isEmpty()) {
                db.deviceDao().insert(Device("AA:BB:CC:DD:EE:FF", "Cgmoreda"))
            }
            if (db.actionDao().getAllActions.isEmpty()) {
                db.actionDao().addAction(Action(macAddress = "AA:BB:CC:DD:EE:FF", onLeave =  true, onSight =  true, message = "Hello"))
            }

        }

    }
    private var serviceIntent: Intent? = null
    private fun scheduleJob() {
        // Get the JobScheduler system service
        if (serviceIntent != null) return

        serviceIntent = Intent(
            this,
            NearifyService::class.java
        )
        startService(serviceIntent)
    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
    }

     fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}


//
//
//class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        enableEdgeToEdge()
//
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener {
//            Toast.makeText(this, "FAB clicked!", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, AddDeviceList::class.java)
//            this.startActivity(intent)
//        }
//
//
//
//        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
//
//        // Set an OnNavigationItemSelectedListener on the BottomNavigationView
//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.noti_btn -> {
//                    val intent = Intent(this, ActionNotification::class.java)
//                    this.startActivity(intent)
//                    true
//                }
//                R.id.saved_devices_btn -> {
//                    val intent = Intent(this, SavedDevicesActivity::class.java)
//                    this.startActivity(intent)
//                    true
//                }
//                R.id.add_device_btn -> {
//                    val intent = Intent(this, ActionNotification::class.java)
//                    this.startActivity(intent)
//                    true
//                }
//                R.id.info_btn -> {
//                    val intent = Intent(this, ActionNotification::class.java)
//                    this.startActivity(intent)
//                    true
//                }
//                else -> false
//            }
//        }
//
//
//
//
//
//
//        GlobalScope.launch(Dispatchers.IO) {
//
//            _db_intial =
//                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "nearify-db")
//                    .build()
//
//            if (db.deviceDao().getAllDevices.isEmpty()) {
//                db.deviceDao().insert(Device("AA:BB:CC:DD:EE:FF", "Cgmoreda"))
//            }
//
//        }
//        GlobalScope.launch {
//            connect()
//        }
////        setContent {
////            NearifyTheme {
////                Scaffold(modifier = Modifier.fillMaxSize())
////                { innerPadding ->
//////                    MainScreen(modifier = Modifier.padding(innerPadding))
////                }
////            }
////        }
//    }
//
//    @Suppress("DEPRECATION", "MissingPermission")
//    private suspend fun connect() {
//        delay(2000)
//        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        startActivityForResult(enableBtIntent, 1)
//        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//
//        val pairedDevices = bluetoothAdapter.bondedDevices
//        val devices = db.deviceDao().getAllDevices
//        val to_delete = mutableSetOf<String>()
//        devices.forEach { device ->
//            if(pairedDevices.none {
//                it.address == device.bluetoothMac
//            }){
//                to_delete.add(device.bluetoothMac)
//            }
//        }
//        db.deviceDao().deleteDevices(to_delete)
//        val newInRange = mutableSetOf<String>()
//        for (device in pairedDevices) {
//            val deviceAddress = device.address
//            if (devices.none {
//                    it.bluetoothMac == deviceAddress
//                }) continue
//            // Use deviceName and deviceAddress to identify the paired device
//            lateinit var bluetoothSocket: BluetoothSocket
//            val uuid = device.uuids[0].uuid // UUID of the device’s service
//
//            try {
//                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
//                bluetoothSocket.connect()
//                // Now you are connected, you can start communicating with the device
//                newInRange.add(deviceAddress)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        val newOutOfRange = setOf(*devices.map {
//            it.bluetoothMac
//        }.toTypedArray()) - newInRange;
//        val oldInRange = db.deviceDao().getInRangeDevices.map { it.device.bluetoothMac }
//        val oldOutOfRange = db.deviceDao().getOutOfRangeDevices.map { it.device.bluetoothMac }
//        val notifications = mutableListOf<String>()
//        oldInRange.forEach { device ->
//            if (newOutOfRange.contains(device)) {
//                db.deviceDao().updateDevice(device, false)
//                notifications.addAll(db.actionDao().getLeaveActions(device).map {
//                    it.message
//                })
//            }
//        }
//        oldOutOfRange.forEach { device ->
//            if(newInRange.contains(device)) {
//                db.deviceDao().updateDevice(device, true)
//                notifications.addAll(db.actionDao().getEnterActions(device).map {
//                    it.message
//                })
//            }
//        }
//        val channelId = "notification_channel"
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.notification_icon) // Use an appropriate notification icon
//            .setContentTitle("Somebody is here or isn't")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        // Assuming 'notification' is a list of messages
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notifications.forEachIndexed { index, message ->
//            val notification = notificationBuilder.setContentText(message)
//            val notificationId = index + 1
//            withContext(Dispatchers.Main) {
//                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }
//}

//@Composable
//private fun MainScreen(modifier: Modifier = Modifier) {
//    Column(modifier = modifier) {
//        ButtonsColumn()
//    }
//}
//
//@Preview
//@Composable
//fun previewMainScreen() {
//    NearifyTheme {
//        Scaffold(
//            modifier = Modifier
//                .fillMaxSize()
//        ) { innerPadding ->
//            MainScreen(modifier = Modifier.padding(innerPadding))
//        }
//    }
//}
//
//@Composable
//private fun ButtonsColumn() {
//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .padding(vertical = 200.dp),
//        verticalArrangement = Arrangement.SpaceEvenly,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        GoToNotification()
//        Spacer(modifier = Modifier.height(16.dp)) // Space between buttons
//        GoToAddDevicesList()
//        Spacer(modifier = Modifier.height(16.dp)) // Space between buttons
//        GoToSavedDevicesList()
//    }
//}
//
//
//@Composable
//private fun GoToNotification() {
//    val context = LocalContext.current
//    Button(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(100.dp), // Added horizontal padding
//        onClick = {
//            val intent = Intent(context, ActionNotification::class.java)
//            context.startActivity(intent)
//        },
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFF87CEEB)
//        )
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.Notifications,
//                contentDescription = "Notification Icon",
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(end = 8.dp)
//            )
//            Text(
//                text = "Go To Notification",
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//@Composable
//private fun GoToAddDevicesList() {
//    val context = LocalContext.current
//    Button(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(100.dp), // Added horizontal padding
//        onClick = {
//            val intent = Intent(context, AddDeviceList::class.java)
//            context.startActivity(intent)
//        },
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFF87CEEB)
//        )
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.DevicesOther,
//                contentDescription = "Device Icon",
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(end = 8.dp)
//            )
//            Text(
//                text = "Go To Device",
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//@Composable
//private fun GoToSavedDevicesList() {
//    val context = LocalContext.current
//    Button(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(100.dp), // Added horizontal padding
//        onClick = {
//            val intent = Intent(context, SavedDevicesActivity::class.java)
//            context.startActivity(intent)
//        },
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFF87CEEB)
//        )
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.Save,
//                contentDescription = "Saved Devices Icon",
//                modifier = Modifier
//                    .size(50.dp)
//
//            )
//            Text(
//                text = "View Saved Devices",
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}