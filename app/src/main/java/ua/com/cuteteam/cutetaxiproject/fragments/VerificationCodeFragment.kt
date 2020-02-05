package ua.com.cuteteam.cutetaxiproject.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_verification_code.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel.*

/**
 * A simple [Fragment] subclass.
 */
class VerificationCodeFragment : Fragment(), View.OnClickListener {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_verification_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sms_code_et.setText(authViewModel.smsCode)

        sms_code_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(sms_code: Editable?) {
                log_in_btn.isEnabled = sms_code?.isNotEmpty() ?: false
                authViewModel.smsCode = sms_code.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        log_in_btn.setOnClickListener(this)
        resend_code_btn.setOnClickListener(this)

        log_in_btn.isEnabled = sms_code_et.text?.isNotEmpty() ?: false
        resend_code_btn.isEnabled = false

        authViewModel.state.observe(viewLifecycleOwner, Observer {
            when(it) {
                State.TIME_OUT -> resend_code_btn.isEnabled = true
                else -> {}
            }
        })
    }

    override fun onClick(view: View?) {
        when(view) {
            log_in_btn -> authViewModel.signIn(sms_code_et.text.toString())
            resend_code_btn -> {
                authViewModel.verifyPhoneNumber(authViewModel.phoneNumber)
                resend_code_btn.isEnabled = false
            }
        }
    }
}
