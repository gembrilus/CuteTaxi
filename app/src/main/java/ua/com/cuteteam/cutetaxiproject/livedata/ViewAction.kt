package ua.com.cuteteam.cutetaxiproject.livedata

sealed class ViewAction {


    class PassengerBottomSheetControl(val action: String) : ViewAction() {
        companion object {
            const val SHOW_EXPANDED = "SHOW_EXPANDED"
            const val SHOW_COLLAPSED = "SHOW_COLLAPSED"
        }
    }

}