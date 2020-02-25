package ua.com.cuteteam.cutetaxiproject.permissions

import pub.devrel.easypermissions.EasyPermissions
import android.util.Log
import androidx.fragment.app.FragmentActivity

import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog

class PermissionProvider(private val activity: FragmentActivity) :
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val LOCATION_REQUEST_CODE = 101
        const val CALL_PHONE_REQUEST_CODE = 102
    }

    var onGranted: (() -> Unit)? = null

    var shouldShowDialog = true

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
            LOCATION_REQUEST_CODE -> handleIfPermissionPermanentlyDenied(
                AccessFineLocationPermission()
            )
            CALL_PHONE_REQUEST_CODE -> handleIfPermissionPermanentlyDenied(
                CallPhonePermission()
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

    private fun handleIfPermissionPermanentlyDenied(permission: Permission) {
        if (isPermissionPermanentlyDenied(permission) && shouldShowDialog)
            showPermissionPermanentlyDeniedDialog(permission)
    }

    private fun showPermissionPermanentlyDeniedDialog(permission: Permission) {
        InfoDialog.show(
            activity.supportFragmentManager,
            permission.requiredPermissionDialogTitle,
            permission.requiredPermissionDialogMessage
        ) { shouldShowDialog = false }
    }

    private fun isPermissionPermanentlyDenied(permission: Permission): Boolean {
        return EasyPermissions.permissionPermanentlyDenied(activity, permission.name)
    }
}
