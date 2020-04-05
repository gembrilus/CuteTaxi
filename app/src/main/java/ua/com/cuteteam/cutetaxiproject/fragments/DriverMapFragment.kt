package ua.com.cuteteam.cutetaxiproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map_driver.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.common.prepareDistance
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.dialogs.RateDialog
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.BaseViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel

private const val TAG = "Cute.DriverFragment"

class DriverMapFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), BaseViewModel.getViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
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

        viewModel.countOfOrders.observe(requireActivity(), Observer { count ->
            with(view.cart_badge) {
                visibility = if (count != 0) View.VISIBLE else View.GONE
                text = count.toString()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.activeOrder.observe(requireActivity(), Observer {
            showUI()
            when (it.orderStatus) {
                OrderStatus.CANCELLED -> {
                    viewModel.closeOrder()
                    hideUI()
                    InfoDialog.show(
                        childFragmentManager,
                        getString(R.string.dialog_title_order_is_changed),
                        getString(
                            R.string.dialog_text_order_was_cancelled,
                            it.addressStart?.address,
                            it.addressDestination?.address
                        )
                    )
                }
                OrderStatus.FINISHED -> {
                    viewModel.closeOrder()
                    hideUI()
                    RateDialog.show(
                        childFragmentManager,
                        null
                    ) { ratingBar, fl, b ->
                        // TODO: Write rating
                    }

                }
                OrderStatus.ACTIVE -> {
                    // TODO: Draw routes

                    view?.order_info_price?.text =
                        requireActivity().getString(R.string.currency_UAH, it.price.toString())
                    view?.order_info_distance?.text = prepareDistance(requireActivity(), it)
                    view?.origin_address?.text = it.addressStart?.address
                    view?.dest_address?.text = it.addressDestination?.address
                    view?.invalidate()

                    val startLocation = it.addressStart?.location?.toLatLng()
                    val endLocation = it.addressDestination?.location?.toLatLng()
                    val start = "${startLocation?.latitude},${startLocation?.longitude}"
                    val end = "${endLocation?.latitude},${endLocation?.longitude}"


                }
                else -> {
                }
            }
        })


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

}