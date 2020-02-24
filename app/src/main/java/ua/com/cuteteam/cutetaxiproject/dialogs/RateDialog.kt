package ua.com.cuteteam.cutetaxiproject.dialogs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RatingBar
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_rate_layout.*
import ua.com.cuteteam.cutetaxiproject.R


/**
 * Show a dialog window with a rating.
 * You can to set a callback for processing of rating.
 * You can to set a lambda function that will be invoke when the ratinng is changed.
 * Invoke this class by the static method [RateDialog.show]. It's recommend
 */
class RateDialog : BaseDialog() {

    interface OnRateCallback {
        fun onRate(rating: Float, ratingBar: RatingBar)
    }


    override val layoutResId: Int
        get() = R.layout.dialog_rate_layout
    override val colorStatusResId: Int
        get() = R.color.colorPrimary

    private var rating = 0.0f

    private var rateCallback: OnRateCallback? = null

    private var runOnRatingChanged: ((RatingBar, Float, Boolean) -> Unit)? = null

    fun setOnRateCallback(rateCallback: OnRateCallback?) {
        this.rateCallback = rateCallback
    }

    fun setRunOnRatingChanged(run: ((RatingBar, Float, Boolean) -> Unit)?) {
        runOnRatingChanged = run
    }

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

        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                this.rating = rating
                runOnRatingChanged?.invoke(ratingBar, rating, fromUser)
            }

        btn_rate.setOnClickListener {
            rateCallback?.onRate(rating, ratingBar)
        }

        btn_dismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.SlideUpDownAnimation
    }

    companion object {

        private const val TAG = "CuteTaxi.RateDialog"

        /**
         * Show a dialog window with a rating.
         * You can to set a callback for processing of rating.
         * You can to set a lambda function that will be invoke when the ratinng is changed.
         * @param fm An instance of FragmentManager
         * @param callback An instance of callback object that handles a positive button click.
         * @param run A function are invoked when [OnRatingBarChangeListener] event happens.
        */
        fun show(
            fm: FragmentManager,
            callback: OnRateCallback? = null,
            run: ((RatingBar, Float, Boolean) -> Unit)? = null
        ) = RateDialog().apply {
            setOnRateCallback(callback)
            setRunOnRatingChanged(run)
        }.show(fm, TAG)

    }

}