package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_make_order.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.extentions.getObservedHeight

class MakeOrderFragment : Fragment(), BottomSheetFragment {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_make_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCollapsed()
    }

    fun showCollapsed() {
        id_replaceable_view_container?.removeAllViews()
        val inflater: LayoutInflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =
            inflater.inflate(
                R.layout.bs_make_order_collapsed,
                id_replaceable_view_container,
                false
            )
        id_replaceable_view_container.addView(view)

        Log.d("Make order fragment", "showCollapsed()")
    }

    fun showExpanded() {
        id_replaceable_view_container?.removeAllViews()
        val inflater: LayoutInflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =
            inflater.inflate(
                R.layout.bs_make_order_expanded,
                id_replaceable_view_container,
                false
            )
        id_replaceable_view_container.addView(view)
    }


    fun showAppBar() {
        app_bar.visibility = View.VISIBLE
    }

    fun hideAppBar() {
        app_bar.visibility = View.GONE
    }

    override suspend fun getPeekHeight(): Int {
        return id_replaceable_view_container.getObservedHeight()
    }
}
