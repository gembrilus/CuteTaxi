package ua.com.cuteteam.cutetaxiproject.permissions

import pub.devrel.easypermissions.EasyPermissions
import android.util.Log
import androidx.fragment.app.FragmentActivity

class PermissionProvider(private val activity: FragmentActivity) :
    EasyPermissions.PermissionCallbacks {

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
            requestPermission(permission)
        }
    }

    private fun requestPermission(permission: Permission) {
        EasyPermissions.requestPermissions(
            activity,
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
            activity,
            this
        )
    }

    private fun isPermissionPermanentlyDenied(permission: Permission): Boolean {
        return EasyPermissions.permissionPermanentlyDenied(activity, permission.name)
    }
}
