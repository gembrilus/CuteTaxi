package ua.com.cuteteam.cutetaxiproject.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_auth.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.activities.AuthActivity

/**
 * A simple [Fragment] subclass.
 */
class AuthFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        continue_btn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view) {
            continue_btn -> (activity as AuthActivity).onContinueButtonClicked()
        }
    }
}
