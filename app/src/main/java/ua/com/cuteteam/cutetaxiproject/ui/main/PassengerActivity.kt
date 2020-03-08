package ua.com.cuteteam.cutetaxiproject.ui.main

import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.ui.main.models.BaseViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel

class PassengerActivity : BaseActivity() {

    private val model by lazy {
        ViewModelProvider(this, BaseViewModel.getViewModelFactory(PassengerRepository()))
            .get(PassengerViewModel::class.java)
    }

    override val menuResId: Int get() = R.menu.nav_menu_settings_pass
    override val layoutResId: Int get() = R.layout.activity_passenger

}