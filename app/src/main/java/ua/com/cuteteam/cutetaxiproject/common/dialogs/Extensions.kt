package ua.com.cuteteam.cutetaxiproject.common.dialogs

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ua.com.cuteteam.cutetaxiproject.R


fun Context.showInfoSnackBar(
    viewGroup: ViewGroup,
    text: String,
    actionName: Int? = null,
    action: ((View) -> Unit)? = null
) {
    val snackBarShowTime = action
        ?.let { Snackbar.LENGTH_INDEFINITE }
        ?: Snackbar.LENGTH_LONG

    Snackbar.make(viewGroup, text, snackBarShowTime).apply {
        animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        action?.let { f ->
            actionName?.let { name ->
                setAction(this@showInfoSnackBar.getString(name), f)
            }
        }
        show()
    }
}


fun Context.showErrorSnackBar(viewGroup: ViewGroup) =
    showInfoSnackBar(viewGroup, getString(R.string.error_dialog_title))

