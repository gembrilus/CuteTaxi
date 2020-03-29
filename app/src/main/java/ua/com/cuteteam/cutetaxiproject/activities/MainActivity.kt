package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    companion object {
        private const val AUTH_REQUEST_CODE = 1
        private const val AUTH_RESULT_CODE = 2
    }

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
                //openStartUpActivity()
                GlobalScope.launch(Dispatchers.Main) {
                    if (authViewModel.verifyCurrentUser()) {
                        if (startUpViewModel.appSettingsHelper.role) startDriverActivity()
                        else startPassengerActivity()
                    }
                    else startAuthorization()
                }
            }
        }, 350)
    }

    private fun startAuthorization() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivityForResult(intent, AUTH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != AUTH_REQUEST_CODE && resultCode != AUTH_RESULT_CODE) return

        val user = data?.extras?.get("user") as FirebaseUser
        GlobalScope.launch(Dispatchers.Main) {
            startUpViewModel.initUser(user)
            if (startUpViewModel.appSettingsHelper.role) startDriverActivity()
            else startPassengerActivity()
        }
    }

    private fun startPassengerActivity() {
        val intent = Intent(this, PassengerActivity::class.java)
        startActivity(intent)
    }

    private fun startDriverActivity() {
        val intent = Intent(this, DriverActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
