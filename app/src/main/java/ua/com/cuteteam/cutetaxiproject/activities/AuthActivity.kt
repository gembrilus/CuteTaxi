package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.fragment_verification_code.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.AuthFragment
import ua.com.cuteteam.cutetaxiproject.fragments.VerificationCodeFragment
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.AuthViewModelFactory

class AuthActivity : AppCompatActivity() {

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory())
            .get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_fl, AuthFragment())
            .commit()

        authViewModel.isCodeSent.observe(this, Observer {
            if (it) supportFragmentManager
                .beginTransaction()
                .replace(R.id.auth_fl, VerificationCodeFragment())
                .addToBackStack(null)
                .commit()
        })
        authViewModel.isUserSignIn.observe(this, Observer { if (it) openFakeMap() })
        authViewModel.isVerificationFailed.observe(this, Observer {
            if (it) Toast.makeText(this, getString(R.string.invalid_phone_number_toast), Toast.LENGTH_SHORT).show()
        })
    }

    fun onContinueButtonClicked() {
        authViewModel.verifyPhoneNumber(phone_number_et.text.toString())
    }

    fun onLogInButtonClicked() {
        authViewModel.signIn(sms_code_et.text.toString())
    }

    private fun openFakeMap() {
        val intent = Intent(this, FakeMapActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) finishAffinity()
    }

}
