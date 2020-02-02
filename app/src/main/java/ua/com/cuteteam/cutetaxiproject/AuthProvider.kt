package ua.com.cuteteam.cutetaxiproject

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure()
}

class AuthProvider {
    private val phoneAuthProvider = PhoneAuthProvider.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var verificationId: String
    var authListener: AuthListener? = null

    private val verificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(id, token)
            authListener?.onStarted()
            verificationId = id
        }

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(credential: FirebaseException) {
            authListener?.onFailure()
        }
    }

    fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null

    fun signOutUser() = auth.signOut()

    fun createCredential(smsCode: String) = PhoneAuthProvider.getCredential(verificationId, smsCode)


    fun verifyPhoneNumber(number: String) {
        phoneAuthProvider.verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            verificationStateChangedCallbacks
        )
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(TaskExecutors.MAIN_THREAD, OnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                authListener?.onSuccess()
            } else {
                // Sign in failed, display a message and update the UI
                Log.w(this.javaClass.name, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
            }
        } )
    }
}