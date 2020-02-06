package ua.com.cuteteam.cutetaxiproject.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_phone_number.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel

class PhoneNumberFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_phone_number, container, false)
    }

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phone_number_et.setText(authViewModel.phoneNumber)

        phone_number_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(phoneNumber: Editable?) {
                authViewModel.phoneNumber = phoneNumber.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        continue_btn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view) {
            continue_btn -> authViewModel.verifyPhoneNumber(phone_number_et.text.toString())
        }
    }
}
