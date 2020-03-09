package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_make_order.*
import ua.com.cuteteam.cutetaxiproject.R

class MakeOrderFragment : Fragment() {

    var bottomViewHeight: Int = 0

    private val behavior by lazy { BottomSheetBehavior.from(bottom_sheet) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_make_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBehaviour()
        showCollapsed()
    }

    private fun setBehaviour() {
        behavior.peekHeight = 0
        behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        app_bar.visibility = View.VISIBLE
                        showExpanded()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        app_bar.visibility = View.GONE
                        showCollapsed()
                    }
                }
            }

            var prevOffset = 0.0f
            var isCollapsedCalled = false
            var isExtendedCalled = false
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                when (slideOffset) {
                    in 0.15f..0.25f -> {
                        if ((slideOffset > prevOffset) && !isExtendedCalled) {
                            showExpanded()
                            isExtendedCalled = true
                            isCollapsedCalled = false
                        } else if ((slideOffset < prevOffset) && !isCollapsedCalled) {
                            showCollapsed()
                            isCollapsedCalled = true
                            isExtendedCalled = false
                        }
                        prevOffset = slideOffset
                    }
                    in 0.85..0.95 -> {
                        if ((slideOffset > prevOffset) && !isExtendedCalled) {
                            app_bar.visibility = View.VISIBLE
                            isExtendedCalled = true
                            isCollapsedCalled = false
                        } else if ((slideOffset < prevOffset) && !isCollapsedCalled) {
                            app_bar.visibility = View.GONE
                            isCollapsedCalled = true
                            isExtendedCalled = false
                        }
                        prevOffset = slideOffset
                    }
                }
                prevOffset = slideOffset
            }
        })
    }

    private fun showCollapsed() {
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
                behavior.peekHeight = id_replaceable_view_container.measuredHeight
            }
        })
    }

    private fun showExpanded() {
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
