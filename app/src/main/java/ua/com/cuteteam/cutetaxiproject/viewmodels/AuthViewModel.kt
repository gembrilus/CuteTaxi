package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import ua.com.cuteteam.cutetaxiproject.AuthProvider
import java.util.concurrent.TimeUnit

class AuthViewModel: ViewModel() {

    val authProvider = AuthProvider()

    fun verifyPhoneNumber() {
        authProvider.verifyPhoneNumber()
    }
}