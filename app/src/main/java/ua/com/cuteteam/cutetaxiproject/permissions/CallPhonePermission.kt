package ua.com.cuteteam.cutetaxiproject.permissions

import android.Manifest
import android.content.Context
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.application.AppClass

data class CallPhonePermission(val context: Context = AppClass.appContext()) : Permission(
    context.getString(R.string.call_phone_permission_rationale),
    Manifest.permission.CALL_PHONE,
    PermissionProvider.CALL_PHONE_REQUEST_CODE,
    context.getString(R.string.call_phone_permission_required_dialog_title),
    context.getString(R.string.call_phone_permission_required_dialog_message)
)