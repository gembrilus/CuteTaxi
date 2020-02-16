package ua.com.cuteteam.cutetaxiproject.common.dialogs

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_simple_layout.*
import ua.com.cuteteam.cutetaxiproject.R

class ErrorDialog : BaseDialog() {

    override val layoutResId: Int
        get() = R.layout.dialog_simple_layout
    override val colorStatusResId: Int
        get() = R.color.colorError

    var message: String? = null


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MESSAGE_KEY, message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            message = it.getString(MESSAGE_KEY, null)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ct_dialog_title.text = requireContext().getString(R.string.error_dialog_title)
        ct_dialog_content.text = message
        btn_ok.setOnClickListener { dialog?.dismiss() }
    }

    companion object {

        private const val TAG = "CuteTaxi.ErrorDialog"
        private const val MESSAGE_KEY = "ErrorDialog.MessageKey"

    }


}