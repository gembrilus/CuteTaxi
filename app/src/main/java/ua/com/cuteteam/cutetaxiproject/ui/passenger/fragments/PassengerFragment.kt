package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ua.com.cuteteam.cutetaxiproject.R

class PassengerFragment() : Fragment() {

    private val makeOrderFragment by lazy { MakeOrderFragment() }
//    private val behaviour by lazy { BottomSheetBehavior.from(bottom_sheet) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_passenger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMakeOrderBottomSheet()
    }

    private fun initMakeOrderBottomSheet() {
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, makeOrderFragment)
            .commit()
    }
}