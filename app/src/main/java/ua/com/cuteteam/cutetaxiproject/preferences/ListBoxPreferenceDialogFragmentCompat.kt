package ua.com.cuteteam.cutetaxiproject.preferences

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.preference.PreferenceDialogFragmentCompat
import kotlinx.android.synthetic.main.preference_dialog_listbox.view.*

class ListBoxPreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    private var mValues = mutableSetOf<String>()
    private var adapter: ListBoxAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState
            ?.let {
                mValues.addAll(savedInstanceState.getStringArrayList(SAVE_STATE_ENTRIES)!!)
            }
            ?: {
                val preference = preference as ListBoxPreference
                mValues.clear()
                mValues.addAll(preference.values)
            }.invoke()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(SAVE_STATE_ENTRIES, ArrayList(mValues))
    }

    private fun setValue(value: String) {
        adapter?.add(value)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        adapter = ListBoxAdapter(mValues)

        with(view) {

            with(edit) {

                setSingleLine()
                setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN)
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            val res = text.toString()
                            if (res.isNotEmpty()) {
                                setValue(res)
                                setText("")
                                return@setOnKeyListener true
                            }
                        }
                    return@setOnKeyListener false
                }

            }


            with(list_addresses) {

                adapter = this@ListBoxPreferenceDialogFragmentCompat.adapter

            }

        }

    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            adapter?.removeChecked()
            val preference: ListBoxPreference = preference as ListBoxPreference
            if (preference.callChangeListener(mValues)) {
                preference.values = mValues
            }
        }
    }

    companion object {

        const val TAG = "CuteTaxi.ListBox"

        private const val SAVE_STATE_ENTRIES = "CuteTaxi.savedEntries"

        fun newInstance(key: String): ListBoxPreferenceDialogFragmentCompat =
            ListBoxPreferenceDialogFragmentCompat().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }

    }

}