package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import ua.com.cuteteam.cutetaxiproject.AuthProvider

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(exception: FirebaseException)
}

class AuthViewModel: ViewModel(), AuthListener {

    private val authProvider = AuthProvider().apply { authListener = this@AuthViewModel }

    val isUserSignIn by lazy {
        MutableLiveData<Boolean>(isUserSignedIn())
    }

    val isVerificationFailed = MutableLiveData<Boolean>(false)

    val isCodeSent = MutableLiveData<Boolean>(false)

    fun verifyPhoneNumber(number: String) {
        authProvider.verifyPhoneNumber(number)
    }

    fun isUserSignedIn() = authProvider.isUserSignedIn()

    fun signOut() = authProvider.signOutUser()

    fun signIn(smsCode: String) {
        authProvider.signInWithPhoneAuthCredential(authProvider.createCredential(smsCode))
    }

    override fun onStarted() {
        isCodeSent.value = true
    }

    override fun onSuccess() {
        isUserSignIn.value = true
    }

    override fun onFailure(exception: FirebaseException) {
        if (exception is FirebaseAuthInvalidCredentialsException) isVerificationFailed.value = true
        else Log.w(AuthViewModel::javaClass.name, exception.cause)
    }

}