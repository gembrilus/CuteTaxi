package ua.com.cuteteam.cutetaxiproject.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class AuthStatus {
    class AuthSuccess(credential: PhoneAuthCredential) : AuthStatus()
    class CodeVerify(id: String, token: PhoneAuthProvider.ForceResendingToken) : AuthStatus()
    class SigninSuccess(user: FirebaseUser?) : AuthStatus()
    object AuthFailed : AuthStatus()
    object RequestFailed : AuthStatus()
    object SigninFailed : AuthStatus()
    object Initialized : AuthStatus()
}