package ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_passenger.*
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel

class PassengerFragment() : Fragment() {

    private val viewModel: PassengerViewModel by activityViewModels()
    private val makeOrderFragment by lazy { MakeOrderFragment() }
    private val orderStatusFragment by lazy { OrderStatusFragment() }
    private val behaviour by lazy { BottomSheetBehavior.from(bottom_sheet_container) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_passenger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMakeOrderBottomSheet()

        initOrderStatusBottomSheet()

        viewModel.isOrderAccepted.observe(viewLifecycleOwner, Observer { orderState ->
            if (orderState) {
                initOrderStatusBottomSheet()
            } else {
                initMakeOrderBottomSheet()
            }
        })
    }

    private fun setMakeOrderBehaviour(fragment: MakeOrderFragment) {
        behaviour.peekHeight = 0
        behaviour.addBottomSheetCallback(makeOrderBehaviourCallback)
    }

    private fun setOrderStatusBehaviour() {
        behaviour.removeBottomSheetCallback(makeOrderBehaviourCallback)

    }

    private fun initMakeOrderBottomSheet() {
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, makeOrderFragment)
            .commit()

        viewLifecycleOwner.lifecycleScope.launch {
            bottom_sheet_container.layoutParams.height = view!!.getObservedHeight()
        }

        setMakeOrderBehaviour(makeOrderFragment)

        val treeObserver: ViewTreeObserver = bottom_sheet_container.viewTreeObserver

        if (treeObserver.isAlive) {
            treeObserver.addOnWindowAttachListener(object :
                ViewTreeObserver.OnWindowAttachListener {
                override fun onWindowDetached() {
                }

                override fun onWindowAttached() {
                    if (childFragmentManager.findFragmentById(R.id.bottom_sheet_container) is MakeOrderFragment) {
                        makeOrderFragment.showCollapsed()
                        setPeekHeight(makeOrderFragment)
                    }
                }
            })
        }
    }

    private fun initOrderStatusBottomSheet() {
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, orderStatusFragment)
            .commit()

        setOrderStatusBehaviour()

        val treeObserver: ViewTreeObserver = bottom_sheet_container.viewTreeObserver

        if (treeObserver.isAlive) {
            treeObserver.addOnWindowAttachListener(object :
                ViewTreeObserver.OnWindowAttachListener {
                override fun onWindowDetached() {
                }

                override fun onWindowAttached() {
                    if (childFragmentManager.findFragmentById(R.id.bottom_sheet_container) is OrderStatusFragment) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val peekHeight = orderStatusFragment.getPeekHeight()
                            bottom_sheet_container.layoutParams.height = peekHeight
                            behaviour.peekHeight = peekHeight
                        }

                    }
                }
            })
        }

    }

    private fun setPeekHeight(fragment: BottomSheetFragment) {
        viewLifecycleOwner.lifecycleScope.launch {
            behaviour.peekHeight = fragment.getPeekHeight()
        }

    }

    private val makeOrderBehaviourCallback = object : BottomSheetBehavior.BottomSheetCallback() {
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
}