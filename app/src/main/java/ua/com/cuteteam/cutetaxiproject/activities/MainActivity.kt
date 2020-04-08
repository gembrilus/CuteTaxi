package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.HeadPieceFragment
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.StartUpViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.AuthViewModelFactory
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.StartUpViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory())
            .get(AuthViewModel::class.java)
    }

    private val startUpViewModel by lazy {
        ViewModelProvider(this, StartUpViewModelFactory())
            .get(StartUpViewModel::class.java)
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
                GlobalScope.launch(Dispatchers.Main) {
                    if (authViewModel.verifyCurrentUser()) {
                        authViewModel.firebaseUser?.let { updateUserAndStartActivity(it) }
                    }
                    else startAuthorization()
                }
            }
        }, 350)
    }

    private fun startAuthorization() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    private suspend fun updateUserAndStartActivity(firebaseUser: FirebaseUser) {
        startUpViewModel.updateOrCreateUser(firebaseUser)
        if (startUpViewModel.isDriver()) startDriverActivity()
        else startPassengerActivity()
    }

    private fun startPassengerActivity() {
        val intent = Intent(this, PassengerActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startDriverActivity() {
        val intent = Intent(this, DriverActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
