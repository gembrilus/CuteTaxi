package ua.com.cuteteam.cutetaxiproject.common.dialogs

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
    var run: ((View) -> Unit)? = null


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TITLE_KEY, title)
        outState.putString(MESSAGE_KEY, message)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        title = savedInstanceState?.getString(TITLE_KEY, null)
        message = savedInstanceState?.getString(MESSAGE_KEY, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ct_dialog_title.text = title
        ct_dialog_content.text = message
        btn_ok.setOnClickListener { run?.invoke(view) ?: dialog?.dismiss() }
    }

    companion object {

        private const val TAG = "CuteTaxi.InfoDialog"
        private const val TITLE_KEY = "InfoDialog.TitleKey"
        private const val MESSAGE_KEY = "InfoDialog.MessageKey"

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
            run: ((View) ->  Unit)? = null) = InfoDialog().apply {
            this.title = title
            this.message = message
            this.run = run
        }.show(fm, TAG)

    }

}