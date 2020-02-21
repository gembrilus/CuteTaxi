package ua.com.cuteteam.cutetaxiproject.extentions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

/**
 * Show info dialog in bootom of the screen - SnackBar. You can set an action for it.
 * @param viewGroup Parent view for binding Snackbar
 * @param text Text message that will be shown
 * @param actionName Text name for the action if it is not null!
 * @param action Lambda-function that runs the action you need. It has an argument View that is SnackBar.
 */
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

