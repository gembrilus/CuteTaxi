package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ua.com.cuteteam.cutetaxiproject.R

class PassBottomSheetFragment : Fragment() {

    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.passenger_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        behavior = BottomSheetBehavior.from(view)
    }

    override fun onStart() {
        super.onStart()
        /*behavior.state = BottomSheetBehavior.STATE_EXPANDED*/
    }
}