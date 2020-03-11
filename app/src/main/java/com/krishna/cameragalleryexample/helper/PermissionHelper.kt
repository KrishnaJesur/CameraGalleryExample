package com.krishna.cameragalleryexample.helper

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission

object PermissionHelper {
    fun checkPermission(activity: Activity, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(activity, permission) == PERMISSION_GRANTED
        }
        return true
    }

    fun askPermission(activity: Activity, permission: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

    fun askPermission(activity: Activity, permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(activity, permissions, requestCode)
        }
    }

    fun showRational(activity: Activity, permission: String): Boolean {
        return shouldShowRequestPermissionRationale(activity, permission)
    }
}