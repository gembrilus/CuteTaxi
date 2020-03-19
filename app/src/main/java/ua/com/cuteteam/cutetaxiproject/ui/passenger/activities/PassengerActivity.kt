package ua.com.cuteteam.cutetaxiproject.ui.passenger.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments.MakeOrderFragment
import ua.com.cuteteam.cutetaxiproject.ui.passenger.fragments.OrderStatusFragment

class PassengerActivity : AppCompatActivity(), OnMapReadyCallback {

    private val makeOrderFragment by lazy { MakeOrderFragment() }
    private val orderStatusFragment by lazy { OrderStatusFragment() }
    private val mapFragment by lazy { SupportMapFragment.newInstance() }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

//        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_container)

/*        bottomSheetBehavior.addBottomSheetCallback(object :
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
                        } else if ((slideOffset < prevOffset) && !isCollapsedCalled) {
                            showTaxiOrderCollapsed()
                            isCollapsedCalled = true
                            isExtendedCalled = false
                        }
                    }
                }
                prevOffset = slideOffset
            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> showTaxiOrderExpanded()
                    BottomSheetBehavior.STATE_COLLAPSED -> showTaxiOrderCollapsed()
                }
            }
        })*/

        supportFragmentManager.beginTransaction()
            .replace(R.id.map, SupportMapFragment())

/*        if (map != null) {
            supportFragmentManager.beginTransaction().replace(R.id.map, mapFragment)
                .commit()
            mapFragment.getMapAsync(this)
        */}

/*        if (bottom_sheet_container != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_sheet_container, makeOrderFragment)
                .commit()

            bottom_sheet_container.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    bottom_sheet_container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    makeOrderFragment.showCollapsed()
                    bottomSheetBehavior.peekHeight = makeOrderFragment.bottomViewHeight ?: BottomSheetBehavior.PEEK_HEIGHT_AUTO
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            })
        }
    }*/

    override fun onMapReady(gMap: GoogleMap) {
        gMap.uiSettings.isZoomControlsEnabled = true
    }

/*    private fun showTaxiOrderCollapsed() {
        makeOrderFragment.showCollapsed()
        bottomSheetBehavior.peekHeight = makeOrderFragment.bottomViewHeight ?: BottomSheetBehavior.PEEK_HEIGHT_AUTO
    }

    private fun showTaxiOrderExpanded() {
        makeOrderFragment.showExpanded()
    }

    private fun showOrderStatus() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, orderStatusFragment)
            .commit()
    }*/
}