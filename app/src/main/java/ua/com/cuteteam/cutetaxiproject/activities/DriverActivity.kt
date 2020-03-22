package ua.com.cuteteam.cutetaxiproject.activities

import android.content.SharedPreferences
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.showInfoSnackBar
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import ua.com.cuteteam.cutetaxiproject.fragments.adapters.OrdersAdapter
import ua.com.cuteteam.cutetaxiproject.viewmodels.BaseViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel

class DriverActivity : BaseActivity(), OrdersAdapter.OnOrderAccept {

    private val model by lazy {
        ViewModelProvider(this, BaseViewModel.getViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
    }

    override val menuResId: Int get() = R.menu.nav_menu_settings_driver
    override val layoutResId: Int get() = R.layout.activity_driver
    override fun onNetworkAvailable() =  show()
    override fun onNetworkLost() = hide()
    override fun onHasActiveOrder(orderId: String?) {

        // TODO: Receive order from Room
        // TODO: Subscribe to order... Rebuild route in DriverMapFragment


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
        model.getOrders()
    }

    private fun show() {
        val parent: ConstraintLayout? = findViewById(R.id.map_container)
        val topLeftInfoBar = parent?.findViewById<MaterialTextView>(R.id.order_info_price)
        val topRightInfoBar = parent?.findViewById<MaterialTextView>(R.id.order_info_distance)
        val actionButton = parent?.findViewById<FloatingActionButton>(R.id.btn_orders_list)
        val badge = parent?.findViewById<MaterialTextView>(R.id.cart_badge)
        topLeftInfoBar?.visibility = View.VISIBLE
        topRightInfoBar?.visibility = View.VISIBLE
        actionButton?.visibility = View.VISIBLE
        badge?.visibility = View.VISIBLE
    }


    private fun hide() {
        val parent: ConstraintLayout? = findViewById(R.id.map_container)
        val topLeftInfoBar = parent?.findViewById<MaterialTextView>(R.id.order_info_price)
        val topRightInfoBar = parent?.findViewById<MaterialTextView>(R.id.order_info_distance)
        val actionButton = parent?.findViewById<FloatingActionButton>(R.id.btn_orders_list)
        val badge = parent?.findViewById<MaterialTextView>(R.id.cart_badge)

        topLeftInfoBar?.visibility = View.GONE
        topRightInfoBar?.visibility = View.GONE
        actionButton?.visibility = View.GONE
        badge?.visibility = View.GONE

        parent?.let {
            showInfoSnackBar( it,"No internet connection!")
        }
    }

}