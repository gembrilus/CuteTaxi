package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.PhoneNumberFragment
import ua.com.cuteteam.cutetaxiproject.fragments.VerificationCodeFragment
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel.*
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.AuthViewModelFactory

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val VERIFICATION_CODE_FRAGMENT = "VERIFICATION_CODE_FRAGMENT"
        private const val PHONE_NUMBER_FRAGMENT = "PHONE_NUMBER_FRAGMENT"
    }

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory())
            .get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        authViewModel.state.observe(this, Observer {
            val transaction = supportFragmentManager.beginTransaction()
            when (it) {
                State.ENTERING_PHONE_NUMBER -> transaction.replace(
                    R.id.auth_fl,
                    PhoneNumberFragment(),
                    PHONE_NUMBER_FRAGMENT
                )
                    .addToBackStack(PHONE_NUMBER_FRAGMENT)
                    .commit()
                State.ENTERING_VERIFICATION_CODE -> transaction.replace(
                    R.id.auth_fl,
                    VerificationCodeFragment(),
                    VERIFICATION_CODE_FRAGMENT
                )
                    .addToBackStack(VERIFICATION_CODE_FRAGMENT)
                    .commit()
                State.LOGGED_IN -> returnToStartUpActivity()
                State.RESEND_CODE -> authViewModel.resendVerificationCode()
                else -> {
                }
            }
        })
    }

    private fun returnToStartUpActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", authViewModel.firebaseUser)
        setResult(2, intent)
        finish()
    }

    override fun onBackPressed() {
        val verificationCodeFragment =
            supportFragmentManager.findFragmentByTag(VERIFICATION_CODE_FRAGMENT)
        if (verificationCodeFragment?.isVisible == true) authViewModel.backToEnteringPhoneNumber()
        else finishAffinity()
    }
}
