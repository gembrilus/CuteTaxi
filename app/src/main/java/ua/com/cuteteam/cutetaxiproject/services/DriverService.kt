package ua.com.cuteteam.cutetaxiproject.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class DriverService : Service(){

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}