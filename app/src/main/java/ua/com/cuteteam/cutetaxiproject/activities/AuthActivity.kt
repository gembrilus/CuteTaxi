package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
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

    private val authorizationObserver = Observer<Boolean> {
        if (it) openFakeMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_fl, AuthFragment())
            .commit()

        authViewModel.isUserSignIn.observe(this, authorizationObserver)
    }

    fun onContinueButtonClicked() {
        authViewModel.verifyPhoneNumber(phone_number_et.text.toString())
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_fl, VerificationCodeFragment())
            .commit()
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
        finishAffinity()
    }

}
