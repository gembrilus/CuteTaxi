package ua.com.cuteteam.cutetaxiproject.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class AuthRequestCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    private var _status = MutableLiveData<AuthStatus>().apply {
        value = AuthStatus.Initialized
    }

    val status get() = _status as LiveData<AuthStatus>

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        Log.d(PhoneAuthClient.TAG, "OnVerificationCompleted: $credential")
        _status.value = AuthStatus.AuthSuccess(credential)
    }

    override fun onVerificationFailed(e: FirebaseException) {
        Log.w(PhoneAuthClient.TAG, "onVerificationFailed", e)

        when (e) {
            is FirebaseAuthInvalidCredentialsException -> _status.value = AuthStatus.AuthFailed
            is FirebaseTooManyRequestsException -> _status.value = AuthStatus.RequestFailed
        }

    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        Log.d(PhoneAuthClient.TAG, "onCodeSent:$verificationId")
        _status.value = AuthStatus.CodeVerify(verificationId, token)
    }

}