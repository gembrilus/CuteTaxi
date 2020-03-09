package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_make_order.*
import ua.com.cuteteam.cutetaxiproject.R

class MakeOrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_make_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_make_order.setOnClickListener {

            if (et_start_address.text.isNullOrEmpty()) {
                et_start_address.error = getString(R.string.error_start_address_empty)
            }
        }
    }
}
