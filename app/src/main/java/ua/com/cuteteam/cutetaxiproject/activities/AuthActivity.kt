package ua.com.cuteteam.cutetaxiproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.AuthFragment

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_fl, AuthFragment())
            .commit()
    }
}
