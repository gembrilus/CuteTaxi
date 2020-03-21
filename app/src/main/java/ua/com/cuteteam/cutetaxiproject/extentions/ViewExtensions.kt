package ua.com.cuteteam.cutetaxiproject.extentions

import android.view.View
import android.view.ViewTreeObserver
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun View.getObservedHeight(): Int {

    val viewTreeObserver = this.viewTreeObserver

    return suspendCancellableCoroutine { continuation ->
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (continuation.isActive) {
                    continuation.resume(measuredHeight)
                    if (viewTreeObserver.isAlive) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }

                continuation.invokeOnCancellation {
                    if (viewTreeObserver.isAlive) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        })
    }
}