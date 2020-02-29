package ua.com.cuteteam.cutetaxiproject.permissions

import android.Manifest
import android.content.Context
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.application.AppClass

data class AccessFineLocationPermission(val context: Context = AppClass.appContext()) : Permission(
    context.getString(R.string.location_permission_rationale),
    Manifest.permission.ACCESS_FINE_LOCATION,
    PermissionProvider.LOCATION_REQUEST_CODE,
    context.getString(R.string.location_permission_required_dialog_title),
    context.getString(R.string.location_permission_required_dialog_message)
)
