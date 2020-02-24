package ua.com.cuteteam.cutetaxiproject.dialogs

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import ua.com.cuteteam.cutetaxiproject.R


/**
 * It is simple the base abstract class for dialogs in the app.
 */
abstract class BaseDialog : DialogFragment() {

    abstract val layoutResId: Int
    abstract val colorStatusResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutResId, container, false)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(0))
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            window?.attributes?.windowAnimations = R.style.SlideUpDownAnimation
        }.also {
            val divider = view?.findViewById<View>(R.id.divider)
            divider?.background = requireContext().getDrawable(colorStatusResId)
        }
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog?.cancel()
        }
        super.onDestroyView()
    }

}