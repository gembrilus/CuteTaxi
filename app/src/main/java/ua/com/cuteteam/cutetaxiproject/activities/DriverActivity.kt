package ua.com.cuteteam.cutetaxiproject.activities

import android.app.Service
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_map_driver.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.extentions.showInfoSnackBar
import ua.com.cuteteam.cutetaxiproject.fragments.adapters.OrdersAdapter
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.services.ACCEPTED_ORDER_ID
import ua.com.cuteteam.cutetaxiproject.services.DriverService
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.DriverViewModelFactory

class DriverActivity : BaseActivity(), OrdersAdapter.OnOrderAccept {

    override val model by lazy {
        ViewModelProvider(this, DriverViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
    }

    override val menuResId: Int get() = R.menu.nav_menu_settings_driver
    override val layoutResId: Int get() = R.layout.activity_driver
    override val service: Class<out Service>
        get() = DriverService::class.java

    override fun onNetworkAvailable() = restoreStateVisibility()
    override fun onNetworkLost() = hide()

    override fun onHasActiveOrder(orderId: String?) {
        model.subscribeOnOrder(orderId)
        navController.setGraph(R.navigation.nav_graph_driver)
    }
    override fun onNoActiveOrder() = model.openHomeOrOrders()

    override fun onAccept(orderId: String) {
        model.obtainOrder(orderId)
        navController.navigate(R.id.action_new_orders_to_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.openHomeOrOrders.observe(this, Observer{
            if (!it) {
                navController.navigate(R.id.action_home_to_new_orders)
            }
        })
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        model.updateOrders()
    }

    override fun onResume() {
        super.onResume()
        intent.getStringExtra(ACCEPTED_ORDER_ID)?.let {
            onAccept(it)
        }
    }

    private fun hide() {
        saveStateVisibility()
        info_boxes?.visibility = View.GONE
        bottom_sheet?.visibility = View.GONE
        btn_orders_list?.visibility = View.GONE
        btn_order_accept?.visibility = View.GONE
        cart_badge?.visibility = View.GONE

        container?.let {
            showInfoSnackBar(it, "No internet connection!")
        }
    }

    private fun saveStateVisibility(){
        val map = mapOf(
            info_boxes to info_boxes.visibility,
            bottom_sheet to bottom_sheet.visibility,
            btn_orders_list to btn_orders_list.visibility,
            btn_order_accept to btn_order_accept.visibility,
            cart_badge to cart_badge.visibility
        )
        model.mapOfVisibility = map
    }

    private fun restoreStateVisibility(){
        val map = model.mapOfVisibility ?: return
        info_boxes?.visibility = map.getValue(info_boxes)
        bottom_sheet?.visibility = map.getValue(bottom_sheet)
        btn_orders_list?.visibility = map.getValue(btn_orders_list)
        btn_order_accept?.visibility = map.getValue(btn_order_accept)
        cart_badge?.visibility = map.getValue(cart_badge)
    }

}