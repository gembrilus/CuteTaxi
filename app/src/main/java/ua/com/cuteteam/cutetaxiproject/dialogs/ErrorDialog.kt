package ua.com.cuteteam.cutetaxiproject.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_simple_layout.*
import ua.com.cuteteam.cutetaxiproject.R


/**
 * Show an error dialog window. It has only one button OK.
 * It has a preinstalled title named "Error". You can redefined it in resources -> strings.
 * Invoke this class by the static method [ErrorDialog.show]. It's recommend
 */
class ErrorDialog : BaseDialog() {

    override val layoutResId: Int
        get() = R.layout.dialog_simple_layout
    override val colorStatusResId: Int
        get() = R.color.colorError

    var message: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ct_dialog_title.text = requireContext().getString(R.string.error_dialog_title)
        ct_dialog_content.text = message
        btn_ok.setOnClickListener { dialog?.dismiss() }
    }

    /**
     * Show an error dialog window.
     * @param fm An instance of FragmentManager
     * @param message Error message
     */
    companion object {

        private const val TAG = "CuteTaxi.ErrorDialog"

        fun show(fm: FragmentManager, message: String) = ErrorDialog().apply {
            this.message = message
        }.show(fm, TAG)

    }


}