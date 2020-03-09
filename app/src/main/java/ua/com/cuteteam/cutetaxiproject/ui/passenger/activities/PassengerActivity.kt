package ua.com.cuteteam.cutetaxiproject.ui.passenger.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_passenger.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments.MakeOrderBottomSheet
import ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments.MakeOrderFragment

class PassengerActivity : AppCompatActivity(), OnMapReadyCallback {

    private val orderTaxiFragment by lazy { MakeOrderFragment() }
    private val orderBottomFragment by lazy { MakeOrderBottomSheet() }
    private val mapFragment by lazy { SupportMapFragment.newInstance() }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_container)
        bottomSheetBehavior.peekHeight = 500
        showTaxiOrderCollapsed()

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            var prevOffset = 0.0f
            var isCollapsedCalled = false
            var isExtendedCalled = false
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                when (slideOffset) {
                    in 0.15f..0.25f -> {
                        if ((slideOffset > prevOffset) && !isExtendedCalled) {
                            showTaxiOrderExpanded()
                            isExtendedCalled = true
                            isCollapsedCalled = false
//                            Log.d("BottomSheet:", "OnSlide Called Expanded")
                        } else if ((slideOffset < prevOffset) && !isCollapsedCalled) {
                            showTaxiOrderCollapsed()
                            isCollapsedCalled = true
                            isExtendedCalled = false
//                            Log.d("BottomSheet:", "OnSlide Called Collapsed")
                        }
                    }
                }
                prevOffset = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> showTaxiOrderExpanded()
                    BottomSheetBehavior.STATE_COLLAPSED -> showTaxiOrderCollapsed()
                }

            }
        })

        supportFragmentManager.beginTransaction()
            .replace(R.id.map, SupportMapFragment())

        if (map != null) {
            supportFragmentManager.beginTransaction().replace(R.id.map, mapFragment)
                .commit()
            mapFragment.getMapAsync(this)
        }

    }

    override fun onMapReady(gMap: GoogleMap) {
        gMap.uiSettings.isZoomControlsEnabled = true

    }

    private fun showTaxiOrderCollapsed() {
        if (!orderBottomFragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_sheet_container, orderBottomFragment)
                .commit()
            Log.d("BottomSheet:", "Called Collapsed")
        }
    }

    private fun showTaxiOrderExpanded() {
        if (!orderTaxiFragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_sheet_container, orderTaxiFragment)
                .commit()
            Log.d("BottomSheet:", "Called Expanded")
        }
    }
}

