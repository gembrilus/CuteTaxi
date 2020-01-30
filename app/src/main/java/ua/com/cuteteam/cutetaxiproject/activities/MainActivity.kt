package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.HeadPieceFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object{
        private const val REQUEST_CODE = 1
        private const val AUTH_ACTIVITY_KEY = "authActivity"
        private const val MAIN_ACTIVITY_KEY = "mainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.head_piece_fl, HeadPieceFragment())
            .commit()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                startAuthorization()
            }
        }, 2000)
    }

    private fun startAuthorization() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}
