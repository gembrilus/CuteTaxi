package ua.com.cuteteam.cutetaxiproject.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import ua.com.cuteteam.cutetaxiproject.data.entities.Address

class AddressAutoCompleteAdapter(
    private val context: Context) : BaseAdapter(), Filterable {

    private val addresses: MutableList<Address> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val address = getItem(position) as Address

        return if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
            view.findViewById<TextView>(android.R.id.text1).text = address.address
            view
        } else {
            convertView.findViewById<TextView>(android.R.id.text1).text = address.address
            convertView
        }
    }

    override fun getItem(position: Int): Any {
        return addresses[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return addresses.size
    }

    fun setAddresses(list: List<Address>) {
        addresses.clear()

        if (list.isNotEmpty()) {
            addAll(list)
            notifyDataSetChanged()
        } else {
            notifyDataSetInvalidated()
        }
    }

    fun addAll(list: List<Address>) {
        addresses.addAll(list)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                if (constraint != null) {
                    val results = addresses
                    filterResults.values = results
                    filterResults.count = results.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}