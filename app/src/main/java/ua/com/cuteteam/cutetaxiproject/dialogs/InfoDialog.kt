package ua.com.cuteteam.cutetaxiproject.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_simple_layout.*
import ua.com.cuteteam.cutetaxiproject.R

/**
 * Show a dialog window with any information. It has only one button OK.
 * It may be a license info or info message for user.
 * Invoke this class by the static method [InfoDialog.show]. It's recommend
 * For errors use [ErrorDialog]
 */
class InfoDialog : BaseDialog() {

    override val layoutResId: Int
        get() = R.layout.dialog_simple_layout
    override val colorStatusResId: Int
        get() = R.color.colorPrimary

    var title: String? = null
    var message: String? = null
    var onClose: ((Dialog?) -> Unit)? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ct_dialog_title.text = title
        ct_dialog_content.text = message
        btn_ok.setOnClickListener {
            onClose?.invoke(dialog)
            dialog?.dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        onClose?.invoke(this.dialog)
        super.onCancel(dialog)
    }

    companion object {

        private const val TAG = "CuteTaxi.InfoDialog"

        /**
         * Show a dialog window with any information.
         * @param fm An instance of FragmentManager
         * @param title A dialog title
         * @param message An info message of the dialog
         * @param run A function that handles an OK button click
         */
        fun show(
            fm: FragmentManager,
            title: String,
            message: String,
            run: ((Dialog?) ->  Unit)? = null) = InfoDialog().apply {
            this.title = title
            this.message = message
            this.onClose = run
        }.show(fm, TAG)

    }

}