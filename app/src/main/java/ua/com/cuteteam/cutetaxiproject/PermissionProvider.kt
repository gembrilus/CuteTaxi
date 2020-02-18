package ua.com.cuteteam.cutetaxiproject

import android.app.Activity
import pub.devrel.easypermissions.EasyPermissions
import android.Manifest

open class Permission(open val rationale: String, val permission: String, val requestCode: Int)
data class AccessFineLocationPermission(override val rationale: String) : Permission(
    rationale,
    Manifest.permission.ACCESS_FINE_LOCATION,
    PermissionProvider.LOCATION_REQUEST_CODE
)

data class CallPhonePermission(override val rationale: String) : Permission(
    rationale,
    Manifest.permission.CALL_PHONE,
    PermissionProvider.CALL_PHONE_REQUEST_CODE
)

class PermissionProvider(private val activity: Activity) : EasyPermissions.PermissionCallbacks {

    companion object {
        const val LOCATION_REQUEST_CODE = 1
        const val CALL_PHONE_REQUEST_CODE = 2
    }

    var onGranted: (() -> Unit)? = null

    fun withPermission(permission: Permission, callback: () -> Unit) {
        if (EasyPermissions.hasPermissions(activity, permission.permission))
            callback()
        else EasyPermissions.requestPermissions(
            activity,
            permission.rationale,
            permission.requestCode,
            permission.permission
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        onGranted?.invoke()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            activity,
            this
        )
    }

}