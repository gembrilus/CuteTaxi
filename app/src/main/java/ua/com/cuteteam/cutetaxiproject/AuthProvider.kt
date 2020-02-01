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

class AuthProvider {
    private val phoneAuthProvider = PhoneAuthProvider.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val verificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("authProvider", "onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(credential: FirebaseException) {
            Log.d(this.javaClass.name, "onVerificationFailed")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            val credential = PhoneAuthProvider.getCredential(verificationId, "888999")
            signInWithPhoneAuthCredential(credential)
        }
    }

    fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null

    fun verifyPhoneNumber() {
        phoneAuthProvider.verifyPhoneNumber(
            "+1 555-666-7777",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            verificationStateChangedCallbacks
        )
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(TaskExecutors.MAIN_THREAD, OnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(this.javaClass.name, "signInWithCredential:success")

                val user = task.result?.user
                // ...
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