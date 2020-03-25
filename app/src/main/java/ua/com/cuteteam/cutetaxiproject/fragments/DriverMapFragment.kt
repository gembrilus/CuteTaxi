package ua.com.cuteteam.cutetaxiproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_map_driver.view.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.dialogs.RateDialog
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.BaseViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel

private const val TAG = "Cute.DriverFragment"

class DriverMapFragment : MapFragment() {

    private val model by lazy {
        ViewModelProvider(requireActivity(), BaseViewModel.getViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
    }

    override fun onCreateView(
        inflator: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parentView = super.onCreateView(inflator, parent, savedInstanceState)
        val new = inflator
            .inflate(R.layout.fragment_map_driver, parent, false)
        new.map_container.addView(parentView, 0)
        return new
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btn_orders_list.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_new_orders)
        }
        model.countOfOrders.observe(requireActivity(), Observer { count ->
            Log.d(TAG, count.toString() )
            with(view.cart_badge) {
                visibility = if (count != 0) View.VISIBLE else View.GONE
                text = count.toString()
            }
        })

        model.activeOrder.observe(requireActivity(), Observer {
            when (it.orderStatus) {
                OrderStatus.CANCELLED ->{
                    model.closeOrder(mMap)
                    InfoDialog.show(
                        requireActivity().supportFragmentManager,
                        getString(R.string.dialog_title_order_is_changed),
                        getString(
                            R.string.dialog_text_order_was_cancelled,
                            it.addressStart?.address,
                            it.addressDestination?.address
                        )
                    )
                }
                OrderStatus.FINISHED ->{
                    model.closeOrder(mMap)
                    RateDialog.show(
                        requireActivity().supportFragmentManager,
                        null
                    ) { ratingBar, fl, b ->
                        // TODO: Write rating
                    }
                }
                OrderStatus.ACTIVE -> {
                    // TODO: Draw routes, fill info boxes

                    val startLocation = it.addressStart?.location?.toLatLng()
                    val endLocation = it.addressDestination?.location?.toLatLng()
                    val start = "${startLocation?.latitude},${startLocation?.longitude}"
                    val end = "${endLocation?.latitude},${endLocation?.longitude}"
                    model.buildRoute(start, end).observe(requireActivity(), Observer {
                        mMap?.addMarker(MarkerOptions().apply {
                            startLocation?.let { startL -> position(startL) }
                        })
                        mMap?.addPolyline(PolylineOptions().apply {
                            add(*it.polyline)
                        })
                        mMap?.addMarker(MarkerOptions().apply {
                            endLocation?.let { endL -> position(endL) }
                        })
                    })
                }
                else -> {}
            }
        })
    }
}