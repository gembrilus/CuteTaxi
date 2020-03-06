package ua.com.cuteteam.cutetaxiproject.ui.main

import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.ui.main.models.BaseViewModel
import ua.com.cuteteam.cutetaxiproject.ui.main.models.DriverViewModel

class DriverActivity : BaseActivity() {

    private val model by lazy {
        ViewModelProvider(this, BaseViewModel.getViewModelFactory(PassengerRepository()))
            .get(DriverViewModel::class.java)
    }

    override val menuResId: Int get() = R.menu.main_menu_driver
    override val layoutResId: Int get() = R.layout.activity_driver

}