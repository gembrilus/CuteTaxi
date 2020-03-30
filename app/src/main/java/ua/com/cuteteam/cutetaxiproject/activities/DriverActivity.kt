package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_map_driver.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.showInfoSnackBar
import ua.com.cuteteam.cutetaxiproject.fragments.adapters.OrdersAdapter
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.services.ACCEPTED_ORDER_ID
import ua.com.cuteteam.cutetaxiproject.services.BaseService
import ua.com.cuteteam.cutetaxiproject.services.DriverService
import ua.com.cuteteam.cutetaxiproject.services.ORDER_ID_NAME
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import ua.com.cuteteam.cutetaxiproject.viewmodels.BaseViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel

class DriverActivity : BaseActivity(), OrdersAdapter.OnOrderAccept {

    private val model by lazy {
        ViewModelProvider(this, BaseViewModel.getViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
    }

    override val menuResId: Int get() = R.menu.nav_menu_settings_driver
    override val layoutResId: Int get() = R.layout.activity_driver
    override fun onNetworkAvailable() = show()
    override fun onNetworkLost() = hide()
    override fun onHasActiveOrder(orderId: String?) {
        model.subscribeOnOrder(orderId)
        navController.setGraph(R.navigation.nav_graph_driver)
    }

    override fun onNoActiveOrder() {
        navController.navigate(R.id.action_home_to_new_orders)
    }

    override fun onAccept(order: Order) {
        order.orderStatus = OrderStatus.ACTIVE
        order.carInfo = with(AppSettingsHelper(this)) {
            getString(
                R.string.order_message_from_dirver,
                carBrand,
                carModel,
                carColor,
                carNumber,
                order.arrivingTime
            )
        }
        model.obtainOrder(order)
        navController.navigate(R.id.action_new_orders_to_home)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        when(key){
            getString(R.string.key_send_notifications_preference) ->
                stopService(
                    Intent(this, DriverService::class.java)
                )
            else -> model.updateOrders()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra(ACCEPTED_ORDER_ID)?.let {
            onHasActiveOrder(it)
        }
        stopService(Intent(this, DriverService::class.java))
    }

    override fun onDestroy() {
        val orderId = model.activeOrderId.value
        startService(Intent(this, DriverService::class.java).apply {
            putExtra(ORDER_ID_NAME, orderId)
        })
        super.onDestroy()
    }

    private fun show() {
        info_boxes?.visibility = View.VISIBLE
        bottom_sheet?.visibility = View.VISIBLE
        btn_orders_list?.visibility = View.VISIBLE
        cart_badge?.visibility = View.VISIBLE
    }


    private fun hide() {

        info_boxes?.visibility = View.GONE
        bottom_sheet?.visibility = View.GONE
        btn_orders_list?.visibility = View.GONE
        cart_badge?.visibility = View.GONE

        container?.let {
            showInfoSnackBar(it, "No internet connection!")
        }
    }

}