package ua.com.cuteteam.cutetaxiproject.fragments.passenger

interface BottomSheetFragment {

    fun setOnChildDrawnListener(callback: OnChildDrawnListener)
    fun removeOnChildDrawnListener(callback: OnChildDrawnListener)
}