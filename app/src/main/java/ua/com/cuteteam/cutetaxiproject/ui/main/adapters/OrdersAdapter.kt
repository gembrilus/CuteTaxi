package ua.com.cuteteam.cutetaxiproject.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.driver_orders_item.view.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo

class OrdersAdapter(private var orders: List<Order> = emptyList()) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    private var prev: View? = null
    private var acceptListener: OnOrderAccept? = null
    private var view: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(R.layout.driver_orders_item, parent, false)
            .run(::OrdersViewHolder)

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        view = holder.itemView
        val order = orders[position]
        with(holder.itemView) {
            order_info_price.text = context.getString(R.string.currency_UAH, order.price?.toInt().toString())
            order_info_route.text = context.getString(R.string.units_KM, order.distance?.toInt().toString())
            order_info_distance.text = prepareDistance(order)
            origin_address.text = order.addressStart?.address
            destination_address.text = order.addressDestination?.address
        }
    }

    fun setOrders(list: List<Order>) {
        orders = list
        notifyDataSetChanged()
    }

    fun setAcceptListener(listener: OnOrderAccept){
        acceptListener = listener
    }

    fun setLocation(location: LatLng) {
        orders.forEach {
            it.driverLocation = Coordinates(location.latitude, location.longitude)
            it.arrivingTime = calcDistance(it)?.div(1000)?.toLong()
        }
        notifyDataSetChanged()
    }

    private fun calcDistance(order: Order): Double? {
        val location = order.driverLocation?.latitude?.let {lat ->
            order.driverLocation?.longitude?.let { lon ->
                LatLng(lat, lon)
            }
        }
        val startLat = order.addressStart?.location?.latitude
        val startLon = order.addressStart?.location?.longitude
        if (startLat != null && startLon != null && location != null) {
            return (location distanceTo LatLng(startLat, startLon))
        }
        return null
    }
    
    private fun prepareDistance(order: Order): String? {
        val d = calcDistance(order) ?: return ""
        return when(d){
            in 0.0..999.0 -> view?.context?.getString(R.string.units_M, d.toInt().toString())
            else -> view?.context?.getString(R.string.units_KM, (d/1000).toInt().toString())
        }
    }

    inner class OrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                showAcceptButton(itemView)
            }
            view.btn_accept.setOnClickListener {
                acceptListener?.onAccept(orders[adapterPosition])
            }
        }

        private fun showAcceptButton(view: View){
            view.btn_accept.visibility = if (view.btn_accept.visibility == View.GONE) {
                if (prev != null){
                    prev?.btn_accept?.visibility = View.GONE
                }
                prev = view
                View.VISIBLE
            }
            else {
                prev = null
                View.GONE
            }
        }
    }

    interface OnOrderAccept{
        fun onAccept(order: Order)
    }

}