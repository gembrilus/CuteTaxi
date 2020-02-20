package ua.com.cuteteam.cutetaxiproject.application

import android.app.Application
import android.content.Context

class AppClass: Application() {

    init {
        instance = this
    }

    companion object {

        private var instance: AppClass? = null

        fun appContext(): Context {
            return instance!!.applicationContext
        }
    }

/*    override fun onCreate() {
        super.onCreate()
    }*/
}
