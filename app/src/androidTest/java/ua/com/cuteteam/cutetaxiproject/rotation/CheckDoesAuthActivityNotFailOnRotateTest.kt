package ua.com.cuteteam.cutetaxiproject.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.cuteteam.cutetaxiproject.activities.AuthActivity

@RunWith(AndroidJUnit4::class)
@LargeTest
class CheckDoesAuthActivityNotFailOnRotateTest: RotationTest() {

    @get:Rule
    val activityRule = ActivityTestRule(AuthActivity::class.java)

    @Test
    fun rotate_check() {
        rotate()
    }

}