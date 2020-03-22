package ua.com.cuteteam.cutetaxiproject.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.cuteteam.cutetaxiproject.activities.PassengerActivity

@RunWith(AndroidJUnit4::class)
@LargeTest
class CheckDoesPassengerActivityNotFailOnRotateTest: RotationTest() {

    @get:Rule
    val activityRule = ActivityTestRule(PassengerActivity::class.java)

    @Test
    fun rotate_check() {
        rotate()
    }
}