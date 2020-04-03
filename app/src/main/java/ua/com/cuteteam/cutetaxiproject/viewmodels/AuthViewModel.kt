package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import ua.com.cuteteam.cutetaxiproject.providers.AuthListener
import ua.com.cuteteam.cutetaxiproject.providers.AuthProvider

class AuthViewModel: ViewModel(),
    AuthListener {

    companion object {
        private const val ERROR_INVALID_PHONE_NUMBER = "ERROR_INVALID_PHONE_NUMBER"
        private const val ERROR_INVALID_VERIFICATION_CODE = "ERROR_INVALID_VERIFICATION_CODE"
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener{
            if (it.currentUser != null) {
                Log.d(AuthViewModel::javaClass.name, "user is not null")
                state.value = State.LOGGED_IN
            }
        }
    }

    enum class State {
        ENTERING_PHONE_NUMBER,
        INVALID_PHONE_NUMBER,
        ENTERING_VERIFICATION_CODE,
        LOGGED_IN,
        RESEND_CODE,
        INVALID_CODE,
        TIME_OUT
    }

    var state = MutableLiveData(State.ENTERING_PHONE_NUMBER)

    private val authProvider = AuthProvider()
        .apply { authListener = this@AuthViewModel }

    val firebaseUser get() = authProvider.user

    var phoneNumber: String = ""
    var smsCode: String = ""

    fun verifyPhoneNumber(number: String) {
        phoneNumber = number
        authProvider.verifyPhoneNumber(number)
    }

    fun resendVerificationCode() {
        authProvider.resendVerificationCode(phoneNumber)
    }

    suspend fun verifyCurrentUser() = authProvider.verifyCurrentUser()

    fun signOut() = authProvider.signOutUser()

    fun signIn(smsCode: String) {
        this.smsCode = smsCode
        authProvider.signInWithPhoneAuthCredential(authProvider.createCredential(smsCode))
    }

    fun backToEnteringPhoneNumber() {
        state.value = State.ENTERING_PHONE_NUMBER
    }

    fun backToEnteringVerificationCode() {
        state.value = State.ENTERING_VERIFICATION_CODE
    }

    override fun onStarted() {
        state.value = State.ENTERING_VERIFICATION_CODE
    }

    override fun onSuccess() {}

    override fun onFailure(errorCode: String) {
        when(errorCode) {
            ERROR_INVALID_PHONE_NUMBER -> state.value = State.INVALID_PHONE_NUMBER
            ERROR_INVALID_VERIFICATION_CODE -> state.value = State.INVALID_CODE
        }
    }

    override fun onResendCode() {
        state.value = State.RESEND_CODE
    }

    override fun onTimeOut() {
        state.value = State.TIME_OUT
    }
}