package com.example.nearify.ui.view

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel


class PermsViewModle(application: Application) : AndroidViewModel(application) {
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirstOrNull();
    }

    fun areAllPermissionsGranted(permissionsToRequest: Array<String>): Boolean {
        return permissionsToRequest.all { permission ->
            ContextCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}