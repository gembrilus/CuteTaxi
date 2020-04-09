package ua.com.cuteteam.cutetaxiproject.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.driver_orders_item.view.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.common.prepareDistance
import ua.com.cuteteam.cutetaxiproject.data.entities.Order

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
            order_info_price.text =
                context.getString(R.string.currency_UAH, order.price?.toInt().toString())
            order_info_route.text =
                context.getString(R.string.units_KM, order.distance?.toInt().toString())
            order_info_distance.text = prepareDistance(view?.context, order)
            origin_address.text = order.addressStart?.address
            destination_address.text = order.addressDestination?.address
        }
    }

    fun setOrders(list: List<Order>) {
        orders = list
        notifyDataSetChanged()
    }

    fun setAcceptListener(listener: OnOrderAccept) {
        acceptListener = listener
    }

    inner class OrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                showAcceptButton(itemView)
            }
            view.btn_accept.setOnClickListener {
                orders[adapterPosition].orderId?.let { id ->
                    acceptListener?.onAccept(id)
                }
            }
        }

        private fun showAcceptButton(view: View) {
            view.btn_accept.visibility = if (view.btn_accept.visibility == View.GONE) {
                if (prev != null) {
                    prev?.btn_accept?.visibility = View.GONE
                }
                prev = view
                View.VISIBLE
            } else {
                prev = null
                View.GONE
            }
        }
    }

    interface OnOrderAccept {
        fun onAccept(orderId: String)
    }

}