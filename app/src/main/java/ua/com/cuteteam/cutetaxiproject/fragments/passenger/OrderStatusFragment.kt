package ua.com.cuteteam.cutetaxiproject.fragments.passenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.bs_order_status.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.mutation
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel

class OrderStatusFragment : Fragment(),
    BottomSheetFragment {

    private val viewModel: PassengerViewModel by activityViewModels()

    private var callback: OnChildDrawnListener? = null

    private val onGlobalLayoutListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            callback?.onChildDrawn(order_status.measuredHeight)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bs_order_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_cancel.setOnClickListener {
            viewModel.activeOrder.mutation {
                it.value?.orderStatus = OrderStatus.CANCELLED
                it.value = null
            }
        }

        viewModel.activeOrder.observe(viewLifecycleOwner, Observer {

            when (it?.orderStatus) {
                OrderStatus.NEW -> showWaitMessage()
                OrderStatus.ACCEPTED -> showDriverMessage(it)
                OrderStatus.STARTED -> showTripMessage(it)
                OrderStatus.FINISHED -> rateTrip(it)
            }
        })
    }

    override fun onStart() {
        order_status.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        super.onStart()
    }

    override fun onPause() {
        order_status.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        super.onPause()
    }

    private fun showWaitMessage() {
        tv_order_info.text = getString(R.string.wait_for_driver_message)
        tv_arriving_time.text = ""
    }

    private fun showDriverMessage(order: Order) {
        tv_order_info.text = order.carInfo
        tv_arriving_time.text = getString(R.string.time_in_minutes, order.arrivingTime)
    }

    private fun showTripMessage(order: Order) {

    }

    private fun rateTrip(order: Order) {

    }

    override fun setOnChildDrawnListener(callback: OnChildDrawnListener) {
        this.callback = callback
    }

    override fun removeOnChildDrawnListener(callback: OnChildDrawnListener) {
        this.callback = null
    }
}