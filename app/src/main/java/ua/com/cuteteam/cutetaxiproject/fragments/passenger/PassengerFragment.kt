package ua.com.cuteteam.cutetaxiproject.fragments.passenger

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_passenger.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.livedata.ViewAction
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel

class PassengerFragment() : Fragment(),
    OnChildDrawnListener {

    private val viewModel: PassengerViewModel by activityViewModels()

    private val makeOrderFragment by lazy {
        MakeOrderFragment().apply {
            setOnChildDrawnListener(this@PassengerFragment)
        }
    }

    private val orderStatusFragment by lazy {
        OrderStatusFragment()
            .apply {
            setOnChildDrawnListener(this@PassengerFragment)
        }
    }

    private val behaviour by lazy { BottomSheetBehavior.from(bottom_sheet_container) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_passenger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            val fragment =
                childFragmentManager.findFragmentById(R.id.bottom_sheet_container) as BottomSheetFragment
            fragment.setOnChildDrawnListener(this)
        }

        if (viewModel.activeOrder.value == null) {
            initMakeOrderBottomSheet()
        } else {
            initOrderStatusBottomSheet()
        }

        viewModel.viewAction.observe(viewLifecycleOwner, Observer { action ->
            if (action is ViewAction.PassengerBottomSheetControl) {
                when (action.action) {
                    ViewAction.PassengerBottomSheetControl.SHOW_COLLAPSED -> showMakeOrderCollapsed()
                    ViewAction.PassengerBottomSheetControl.SHOW_EXPANDED -> showMakeOrderExpanded()
                }
            }
        })

        viewModel.activeOrder.observe(viewLifecycleOwner, Observer { order ->
            if (order != null) {
                initOrderStatusBottomSheet()
            } else {
                initMakeOrderBottomSheet()
            }
        })
    }

    private fun initMakeOrderBottomSheet() {
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, makeOrderFragment)
            .commit()

        behaviour.addBottomSheetCallback(makeOrderBehaviourCallback)
    }

    private fun initOrderStatusBottomSheet() {
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, orderStatusFragment)
            .commit()

        behaviour.removeBottomSheetCallback(makeOrderBehaviourCallback)
    }

    private val makeOrderBehaviourCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        this@PassengerFragment.makeOrderFragment.showAppBar()
                        makeOrderFragment.showExpanded()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        makeOrderFragment.hideAppBar()
                        makeOrderFragment.showCollapsed()
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
                            makeOrderFragment.showExpanded()
                            isExtendedCalled = true
                            isCollapsedCalled = false
                        } else if ((slideOffset < prevOffset) && !isCollapsedCalled) {
                            makeOrderFragment.showCollapsed()
                            isCollapsedCalled = true
                            isExtendedCalled = false
                        }
                        prevOffset = slideOffset
                    }
                    in 0.85..0.95 -> {
                        if ((slideOffset > prevOffset) && !isExtendedCalled) {
                            makeOrderFragment.showAppBar()
                            isExtendedCalled = true
                            isCollapsedCalled = false
                        } else if ((slideOffset < prevOffset) && !isCollapsedCalled) {
                            makeOrderFragment.hideAppBar()
                            isCollapsedCalled = true
                            isExtendedCalled = false
                        }
                        prevOffset = slideOffset
                    }
                }
                prevOffset = slideOffset
            }
        }

    override fun onChildDrawn(childHeight: Int) {
        val parentHeight = view!!.measuredHeight
        val mapLayoutParams = map_container.layoutParams
        val bottomSheetLayoutParams = bottom_sheet_container.layoutParams

        behaviour.peekHeight = childHeight
        mapLayoutParams.height = parentHeight - childHeight

        when (childFragmentManager.findFragmentById(R.id.bottom_sheet_container)) {
            is OrderStatusFragment -> {
                behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetLayoutParams.height = childHeight
            }
            is MakeOrderFragment -> {
                bottomSheetLayoutParams.height = parentHeight
            }
        }

        map_container.layoutParams = mapLayoutParams
        bottom_sheet_container.layoutParams = bottomSheetLayoutParams
    }

    private fun showMakeOrderCollapsed() {
        val fragment = childFragmentManager.findFragmentById(R.id.bottom_sheet_container)
        if (fragment is MakeOrderFragment) {
            makeOrderFragment.hideAppBar()
            behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            makeOrderFragment.showCollapsed()
        }
    }

    private fun showMakeOrderExpanded() {
        val fragment = childFragmentManager.findFragmentById(R.id.bottom_sheet_container)
        if (fragment is MakeOrderFragment) {
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            makeOrderFragment.showExpanded()
            makeOrderFragment.showAppBar()
        }
    }
}