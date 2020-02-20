package ua.com.cuteteam.cutetaxiproject

import pub.devrel.easypermissions.EasyPermissions
import android.Manifest
import android.content.Context
import androidx.fragment.app.FragmentActivity
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.common.dialogs.InfoDialog

open class Permission(
    open val rationale: String, val name: String, val requestCode: Int,
    val requiredPermissionDialogTitle: String,
    val requiredPermissionDialogMessage: String
)

data class AccessFineLocationPermission(val context: Context = AppClass.appContext()) : Permission(
    context.getString(R.string.call_phone_permission_rationale),
    Manifest.permission.ACCESS_FINE_LOCATION,
    PermissionProvider.LOCATION_REQUEST_CODE,
    context.getString(R.string.location_permission_required_dialog_title),
    context.getString(R.string.location_permission_required_dialog_message)
)

data class CallPhonePermission(val context: Context = AppClass.appContext()) : Permission(
    context.getString(R.string.location_permission_rationale),
    Manifest.permission.CALL_PHONE,
    PermissionProvider.CALL_PHONE_REQUEST_CODE,
    context.getString(R.string.call_phone_permission_required_dialog_title),
    context.getString(R.string.call_phone_permission_required_dialog_message)
)

class PermissionProvider(private val activity: FragmentActivity) :
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val LOCATION_REQUEST_CODE = 1
        const val CALL_PHONE_REQUEST_CODE = 2
    }

    var onGranted: (() -> Unit)? = null

    fun withPermission(permission: Permission, callback: () -> Unit) {
        if (EasyPermissions.hasPermissions(activity, permission.name)) {
            callback()
        } else {
            requestPermission(permission)
        }
    }

    private fun requestPermission(permission: Permission) {
        val isPermanentlyDenied = EasyPermissions.somePermissionPermanentlyDenied(
            activity,
            listOf(permission.name)
        )

        if (isPermanentlyDenied) {
            InfoDialog.show(
                activity.supportFragmentManager,
                permission.requiredPermissionDialogTitle,
                permission.requiredPermissionDialogMessage
            )
        } else {
            EasyPermissions.requestPermissions(
                activity,
                permission.rationale,
                permission.requestCode,
                permission.name
            )
        }
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