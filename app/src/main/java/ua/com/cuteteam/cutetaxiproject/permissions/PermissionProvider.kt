package ua.com.cuteteam.cutetaxiproject.permissions

import android.app.Activity
import pub.devrel.easypermissions.EasyPermissions
import android.util.Log
import androidx.fragment.app.Fragment

class PermissionProvider :
    EasyPermissions.PermissionCallbacks {

    private val host: Any
    private val activity: Activity

    constructor(activity: Activity) {
        host = activity
        this.activity = activity
    }

    constructor(fragment: Fragment) {
        host = fragment
        activity = fragment.activity!!
    }

    companion object {
        const val LOCATION_REQUEST_CODE = 101
        const val CALL_PHONE_REQUEST_CODE = 102
    }

    var onGranted: (() -> Unit)? = null
    var onDenied: ((permission: Permission,isPermanentlyDenied: Boolean) -> Unit)? = null

    fun withPermission(permission: Permission, callback: () -> Unit) {

        if (EasyPermissions.hasPermissions(activity, permission.name)) {
            callback()
        } else {
            if (host is Activity) requestPermission(permission, host)
            else if (host is Fragment) requestPermission(permission, host)
        }
    }

    private fun requestPermission(permission: Permission, host: Activity) {
        EasyPermissions.requestPermissions(
            host,
            permission.rationale,
            permission.requestCode,
            permission.name
        )
    }

    private fun requestPermission(permission: Permission, host: Fragment) {
        EasyPermissions.requestPermissions(
            host,
            permission.rationale,
            permission.requestCode,
            permission.name
        )
    }

    override fun onPermissionsDenied(requestCode: Int, permissions: MutableList<String>) {
        Log.d(PermissionProvider::class.java.name, "onPermissionsDenied")
        when (requestCode) {
            LOCATION_REQUEST_CODE -> onDenied?.invoke( AccessFineLocationPermission(),
                isPermissionPermanentlyDenied(AccessFineLocationPermission())
            )
            CALL_PHONE_REQUEST_CODE -> onDenied?.invoke( CallPhonePermission(),
                isPermissionPermanentlyDenied(CallPhonePermission())
            )
        }
    }

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
            host,
            this
        )
    }

    private fun isPermissionPermanentlyDenied(permission: Permission): Boolean {
        return EasyPermissions.permissionPermanentlyDenied(activity, permission.name)
    }
}
