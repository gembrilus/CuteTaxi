package ua.com.cuteteam.cutetaxiproject.preferences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.listbox_checked_item.view.*
import ua.com.cuteteam.cutetaxiproject.R

class ListBoxAdapter(private val list: MutableSet<String>) : BaseAdapter() {

    private val checkedItems = mutableListOf<String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView
            ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.listbox_checked_item, parent, false)

        with(view) {
            isClickable = true
            setOnLongClickListener {
                val item = list.elementAt(position)
                it.cbBox.visibility = if (it.cbBox.isVisible) {
                    checkedItems.remove(item)
                    View.GONE
                } else {
                    checkedItems.add(item)
                    it.cbBox.isChecked = true
                    View.VISIBLE
                }
                true
            }

            item_text.text = list.elementAt(position)
        }

        return view
    }

    override fun getItem(position: Int): Any = list.elementAt(position)
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = list.size


    /**
     * Adds a value to an adapter data list
     * @param value is a value that should be added to the list
     *
     */
    fun add(value: String) {
        list.add(value)
        notifyDataSetChanged()
    }


    /**
     * Removes a value from an adapter data list
     * @param value is a value that should be removed from the list
     *
     */
    fun remove(value: String) {
        list.remove(value)
        notifyDataSetChanged()
    }

    /**
     * Removes checked values from an adapter data list. Values are checked with long click on it
     */
    fun removeChecked() = checkedItems.forEach {
        remove(it)
    }

}