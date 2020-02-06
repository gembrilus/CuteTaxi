package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.PhoneNumberFragment
import ua.com.cuteteam.cutetaxiproject.fragments.VerificationCodeFragment
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel.*
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.AuthViewModelFactory

class AuthActivity : AppCompatActivity() {

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory())
            .get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        authViewModel.state.observe(this, Observer {
            val transaction = supportFragmentManager.beginTransaction()
            when(it) {
                State.ENTERING_PHONE_NUMBER -> transaction.replace(R.id.auth_fl, PhoneNumberFragment(), "AUTH_FRAGMENT")
                    .addToBackStack("AUTH_FRAGMENT")
                    .commit()
                State.INVALID_PHONE_NUMBER -> {
                    makeToast(R.string.invalid_phone_number_toast)
                    authViewModel.backToEnteringPhoneNumber()
                }
                State.ENTERING_VERIFICATION_CODE -> transaction.replace(R.id.auth_fl, VerificationCodeFragment(), "VERIFICATION_CODE_FRAGMENT")
                    .addToBackStack("VERIFICATION_CODE_FRAGMENT")
                    .commit()
                State.LOGGED_IN -> openFakeMap()
                State.RESEND_CODE -> authViewModel.resendVerificationCode()
                State.INVALID_CODE -> {
                    makeToast(R.string.invalid_code_number_toast)
                    authViewModel.backToEnteringVerificationCode()
                }
                else -> {}
            }
        })
    }

    private fun openFakeMap() {
        val intent = Intent(this, FakeMapActivity::class.java)
        startActivity(intent)
    }

    private fun makeToast(id: Int) = Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show()

    override fun onBackPressed() {
        val verificationCodeFragment = supportFragmentManager.findFragmentByTag("VERIFICATION_CODE_FRAGMENT")
        if (verificationCodeFragment?.isVisible == true) authViewModel.backToEnteringPhoneNumber()
        else finishAffinity()
    }

}
