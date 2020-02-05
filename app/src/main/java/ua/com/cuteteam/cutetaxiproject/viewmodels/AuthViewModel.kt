package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.com.cuteteam.cutetaxiproject.AuthListener
import ua.com.cuteteam.cutetaxiproject.AuthProvider



class AuthViewModel: ViewModel(), AuthListener {

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

    private val authProvider = AuthProvider().apply { authListener = this@AuthViewModel }

    var phoneNumber: String = ""
    var smsCode: String = ""

    fun verifyPhoneNumber(number: String) {
        phoneNumber = number
        authProvider.verifyPhoneNumber(number)
    }

    fun resendVerificationCode() {
        authProvider.resendVerificationCode(phoneNumber)
    }

    fun isUserSignedIn() = authProvider.isUserSignedIn()

    fun signOut() = authProvider.signOutUser()

    fun signIn(smsCode: String) {
        this.smsCode = smsCode
        authProvider.signInWithPhoneAuthCredential(authProvider.createCredential(smsCode))
    }

    override fun onStarted() {
        state.value = State.ENTERING_VERIFICATION_CODE
    }

    override fun onSuccess() {
        state.value = State.LOGGED_IN
    }

    override fun onFailure(errorCode: String) {
        when(errorCode) {
            "ERROR_INVALID_PHONE_NUMBER" -> state.value = State.INVALID_PHONE_NUMBER
            "ERROR_INVALID_VERIFICATION_CODE" -> state.value = State.INVALID_CODE
        }
    }

    override fun onResendCode() {
        state.value = State.RESEND_CODE
    }

    override fun onTimeOut() {
        state.value = State.TIME_OUT
    }
}