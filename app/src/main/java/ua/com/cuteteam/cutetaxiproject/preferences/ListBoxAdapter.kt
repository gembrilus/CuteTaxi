package ua.com.cuteteam.cutetaxiproject.preferences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.listbox_checked_item.view.*
import ua.com.cuteteam.cutetaxiproject.R

class ListBoxAdapter(private val list: MutableSet<String>) : BaseAdapter() {

    private val checkedItems = mutableListOf<Int>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView
            ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.listbox_checked_item, parent, false)

        with(view) {
            isClickable = true
            setOnLongClickListener {
                it.cbBox.visibility = if (it.cbBox.isVisible) {
                    checkedItems.remove(position)
                    View.GONE
                } else {
                    checkedItems.add(position)
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

    fun add(value: String) {
        list.add(value)
        notifyDataSetChanged()
    }

    fun remove(value: String) {
        list.remove(value)
        notifyDataSetChanged()
    }

    fun removeChecked() = checkedItems.forEach {
        remove(list.elementAt(it))
    }

}