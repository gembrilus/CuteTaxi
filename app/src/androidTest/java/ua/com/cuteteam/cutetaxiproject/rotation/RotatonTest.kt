package ua.com.cuteteam.cutetaxiproject.rotation

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

abstract class RotationTest {

    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    protected fun rotate() {

        synchronized(device) {
            device.unfreezeRotation()
            repeat(5) {
                device.setOrientationNatural()
                device.waitForWindowUpdate(null, 500)
                device.setOrientationLeft()
                device.waitForWindowUpdate(null, 500)
                device.setOrientationRight()
                device.waitForWindowUpdate(null, 500)
            }
        }
    }
}