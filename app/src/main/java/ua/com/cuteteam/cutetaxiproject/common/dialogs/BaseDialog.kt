package ua.com.cuteteam.cutetaxiproject.common.dialogs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_simple_layout.*
import ua.com.cuteteam.cutetaxiproject.R

/**
 * It is simple the base abstract class for dialogs in the app.
 */
abstract class BaseDialog : DialogFragment() {

    abstract val layoutResId: Int
    abstract val colorStatusResId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutResId, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        divider.background = requireContext().getDrawable(colorStatusResId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.SlideUpDownAnimation
    }

}