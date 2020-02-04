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
            when(it) {
                Companion.State.ENTERING_PHONE_NUMBER -> supportFragmentManager.beginTransaction().replace(R.id.auth_fl, AuthFragment()).commit()
                Companion.State.INVALID_PHONE_NUMBER -> makeToast(R.string.invalid_phone_number_toast)
                Companion.State.ENTERING_VERIFICATION_CODE -> supportFragmentManager.beginTransaction().replace(R.id.auth_fl, VerificationCodeFragment())
                    .addToBackStack(null)
                    .commit()
                Companion.State.LOGGED_IN -> openFakeMap()
                Companion.State.RESEND_CODE -> authViewModel.resendVerificationCode()
                Companion.State.INVALID_CODE -> makeToast(R.string.invalid_code_number_toast)
                Companion.State.TIME_OUT -> resend_code_btn.isEnabled = true
            }
        })
    }

    fun onContinueButtonClicked() {
        authViewModel.verifyPhoneNumber(phone_number_et.text.toString())
    }

    fun onLogInButtonClicked() {
        authViewModel.signIn(sms_code_et.text.toString())
    }

    fun onResendButtonClicked() {
        authViewModel.verifyPhoneNumber(authViewModel.phoneNumber)
        resend_code_btn.isEnabled = false
    }

    private fun openFakeMap() {
        val intent = Intent(this, FakeMapActivity::class.java)
        startActivity(intent)
    }

    private fun makeToast(id: Int) = Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show()


    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) finishAffinity()
    }

}
