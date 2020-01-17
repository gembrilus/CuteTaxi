package ua.com.cuteteam.cutetaxiproject.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.common.api.internal.LifecycleCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine

class PhoneAuthClient (
    context: Context,
    private val mAuth: FirebaseAuth,
    private val mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks) : AuthClient, LifecycleObserver {

    private val mContext: Context

    companion object{

        const val TAG = "PhoneAuthClient"

    }

    init {

        Log.d(TAG, "Authenticator is initialized..")

        if (context is Activity) {
            mContext = context
        } else throw IllegalArgumentException("Context is wrong. One needs an instance of the Activity")

        mAuth.useAppLanguage()

    }

    fun verify(number: String) =
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            mContext as Activity,
            mCallback
        )

    fun signIn(credential: PhoneAuthCredential): AuthStatus {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(mContext as Activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    AuthStatus.SigninSuccess(user)
                } else {
                    val exception = task.exception
                    Log.w(TAG, "signInWithCredential:failure", exception)
                    if (exception is FirebaseAuthInvalidCredentialsException) {

                        AuthStatus.SigninFailed

                    }
                }
            }
    }

    suspend fun PhoneAuthCredential.await(credential: PhoneAuthCredential) : AuthStatus {
        return suspendCoroutine {continuation ->
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mContext as Activity) {task ->
                    if (task.isSuccessful){

                    }
                }
        }
    }

    fun signOut(){}

}