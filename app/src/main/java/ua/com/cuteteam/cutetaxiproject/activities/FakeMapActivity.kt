package ua.com.cuteteam.cutetaxiproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ua.com.cuteteam.cutetaxiproject.R

class FakeMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_map)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
