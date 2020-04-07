package ua.com.cuteteam.cutetaxiproject.fragments.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_map_driver.view.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.common.prepareDistance
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.dialogs.RateDialog
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.DriverViewModelFactory

private const val TAG = "Cute.DriverFragment"

class DriverMapFragment : Fragment() {

    private val model by lazy {
        ViewModelProvider(requireActivity(), DriverViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
    }

    private val ratingCallback by lazy {
        object : RateDialog.OnRateCallback {
            override fun onRate(rating: Float, ratingBar: RatingBar) {
                model.rate(rating)
            }
        }
    }

    private val activeOrderObserver = Observer<Order> {
        when (it?.orderStatus) {
            OrderStatus.CANCELLED -> {
                hideUI()
                showCancelDialog()
                model.closeOrder()
            }
            OrderStatus.FINISHED -> {
                hideUI()
                showRateDialog()
                model.closeOrder()
            }
            OrderStatus.ACTIVE -> {
                showUI()
                // TODO: Draw routes

                view?.order_info_price?.text =
                    requireActivity().getString(R.string.currency_UAH, it.price.toString())
                view?.order_info_distance?.text = prepareDistance(requireActivity(), it)
                view?.origin_address?.text = it.addressStart?.address
                view?.dest_address?.text = it.addressDestination?.address
                view?.invalidate()

                val startLocation = it.addressStart?.location?.toLatLng()
                val endLocation = it.addressDestination?.location?.toLatLng()

            }
            else -> hideUI()

        }
    }

    override fun onCreateView(
        inflator: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflator.inflate(R.layout.fragment_map_driver, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideUI()
        view.btn_orders_list.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_new_orders)
        }

        model.countOfOrders.observe(requireActivity(), Observer { count ->
            with(view.cart_badge) {
                visibility = if (count != 0) View.VISIBLE else View.GONE
                text = count.toString()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.activeOrder.observe(this, activeOrderObserver)
    }

    private fun hideUI() {
        view?.info_boxes?.order_info_price?.visibility = View.INVISIBLE
        view?.info_boxes?.order_info_distance?.visibility = View.GONE
        view?.bottom_sheet?.visibility = View.GONE
    }

    private fun showUI() {
        view?.info_boxes?.order_info_price?.visibility = View.VISIBLE
        view?.info_boxes?.order_info_distance?.visibility = View.VISIBLE
        view?.bottom_sheet?.visibility = View.VISIBLE
    }

    private fun showCancelDialog() = activity?.supportFragmentManager?.let {
        InfoDialog.show(
            fm = it,
            title = getString(R.string.dialog_title_order_is_changed),
            message = getString(R.string.dialog_text_order_was_cancelled)
        )
    }

    private fun showRateDialog() = activity?.supportFragmentManager?.let {
        RateDialog.show(
            fm = it,
            callback = ratingCallback
        )
    }

}