package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ua.com.cuteteam.cutetaxiproject.R

class OrderStatusBottomSheet : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bs_order_status, container, false)
    }

}