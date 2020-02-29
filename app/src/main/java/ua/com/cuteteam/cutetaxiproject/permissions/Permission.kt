package ua.com.cuteteam.cutetaxiproject.permissions

open class Permission(
    open val rationale: String, val name: String, val requestCode: Int,
    val requiredPermissionDialogTitle: String,
    val requiredPermissionDialogMessage: String
)