package ua.com.cuteteam.cutetaxiproject.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_verification_code.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.activities.AuthActivity

/**
 * A simple [Fragment] subclass.
 */
class VerificationCodeFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_verification_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log_in_btn.setOnClickListener(this)
        log_in_btn.isEnabled = sms_code_et.text?.isNotEmpty() ?: false
        resend_code_btn.setOnClickListener(this)
        resend_code_btn.isEnabled = false
        sms_code_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(sms_code: Editable?) {
                log_in_btn.isEnabled = sms_code?.isNotEmpty() ?: false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onClick(view: View?) {
        when(view) {
            log_in_btn -> (activity as AuthActivity).onLogInButtonClicked()
            resend_code_btn -> (activity as AuthActivity).onResendButtonClicked()
        }
    }
}
