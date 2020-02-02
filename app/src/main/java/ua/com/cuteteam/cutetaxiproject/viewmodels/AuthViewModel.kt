package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.com.cuteteam.cutetaxiproject.AuthListener
import ua.com.cuteteam.cutetaxiproject.AuthProvider

class AuthViewModel: ViewModel(), AuthListener {

    private val authProvider = AuthProvider().apply { authListener = this@AuthViewModel }

    val isUserSignIn by lazy {
        MutableLiveData<Boolean>(isUserSignedIn())
    }

    fun verifyPhoneNumber(number: String) {
        authProvider.verifyPhoneNumber(number)
    }

    fun isUserSignedIn() = authProvider.isUserSignedIn()

    fun signOut() = authProvider.signOutUser()

    fun signIn(smsCode: String) {
        authProvider.signInWithPhoneAuthCredential(authProvider.createCredential(smsCode))
    }

    override fun onStarted() {

    }

    override fun onSuccess() {
        isUserSignIn.value = true
    }

    override fun onFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}