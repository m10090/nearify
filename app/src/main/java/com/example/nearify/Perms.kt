package com.example.nearify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
// import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nearify.ui.theme.NearifyTheme
import com.example.nearify.ui.view.PermsViewModle
import com.example.nearify.ui.view.composables.BluetoothPermissionTextProvider
import com.example.nearify.ui.view.composables.LocationPermissionTextProvider
import com.example.nearify.ui.view.composables.PermissionDialog
import com.example.nearify.ui.view.composables.pushNotifyTextProvider

class Perms : ComponentActivity() {
    private val permissionsToRequest = arrayOf(
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NearifyTheme {
                val viewModel = viewModel<PermsViewModle>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionsToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        multiplePermissionResultLauncher.launch(permissionsToRequest)
                    }) {

                        Text(text = "Request multiple permission")
                    }
                }

                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                Manifest.permission.BLUETOOTH -> {
                                    BluetoothPermissionTextProvider()
                                }

                                Manifest.permission.POST_NOTIFICATIONS -> {
                                    pushNotifyTextProvider()
                                }

                                Manifest.permission.ACCESS_FINE_LOCATION -> {
                                    LocationPermissionTextProvider()
                                }

                                else -> return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = viewModel::dismissDialog,
                            onOkClick = {
                                viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(permission)
                                )
                            },
                            onGoToAppSettingsClick = ::openAppSettings
                        )
                    }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}