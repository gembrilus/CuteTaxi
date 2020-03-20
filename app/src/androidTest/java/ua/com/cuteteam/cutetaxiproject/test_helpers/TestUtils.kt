package ua.com.cuteteam.cutetaxiproject.test_helpers

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot

fun wait(millis: Long): ViewAction? {
    return object : ViewAction {
        override fun getConstraints() = isRoot()
        override fun getDescription() = "wait during $millis millis."

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis
            do {
                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)
        }
    }
}