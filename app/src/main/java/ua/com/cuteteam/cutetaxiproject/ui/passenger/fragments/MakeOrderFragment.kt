package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_make_order.*
import ua.com.cuteteam.cutetaxiproject.R

class MakeOrderFragment : Fragment() {

    var bottomViewHeight : Int? = null

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

        val viewTreeObserver: ViewTreeObserver = id_replaceable_view_container.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                id_replaceable_view_container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                bottomViewHeight = id_replaceable_view_container.measuredHeight
            }
        })
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
}
