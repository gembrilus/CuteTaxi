package ua.com.cuteteam.cutetaxiproject.ui.passenger.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.replace
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_passenger.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments.PassBottomSheetFragment

class PassengerActivity : AppCompatActivity() {

    private val orderTaxiFragment by lazy { PassBottomSheetFragment() }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_container)

        supportFragmentManager.beginTransaction()
            .replace(R.id.map, SupportMapFragment())

/*        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, orderTaxiFragment)
            .commit()*/

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val callbak = object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}
