package ua.com.cuteteam.cutetaxiproject.ui.passenger.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ua.com.cuteteam.cutetaxiproject.R

class BottomSheet(context: Context, val viewGroup: ViewGroup) : CoordinatorLayout(context) {

    fun show() {
        val inflater: LayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.fragment_make_order, viewGroup, false)
        viewGroup.addView(view)
    }


}