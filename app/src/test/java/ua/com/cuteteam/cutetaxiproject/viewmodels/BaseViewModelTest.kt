package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doReturnConsecutively
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.com.cuteteam.cutetaxiproject.CuteTestCoroutineDispatcher
import ua.com.cuteteam.cutetaxiproject.getOrAwaitValue
import ua.com.cuteteam.cutetaxiproject.helpers.network.NetHelper
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class BaseViewModelTest {

    private var model: BaseViewModel? = null
    private val netHelper: NetHelper = mock()

    private val orderId1 = "12345"
    private val orderId2 = "54321"
    private val spHelper: AppSettingsHelper = mock{
        on { activeOrderId } doReturnConsecutively listOf(orderId1, orderId2)
    }

    private var repository: Repository = mock {
        on { netHelper } doReturn netHelper
        on { spHelper } doReturn spHelper
    }


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(CuteTestCoroutineDispatcher)
        model = BaseViewModel(repository)
    }

    @After
    fun tearDown() {
        model = null
    }

    @Test
    fun checkThatValueOfLiveData_activeOrderId_ReceivesOnlyOneTime() {

        //Firest stage

        //Run
        val first = model!!.activeOrderId.getOrAwaitValue()

        //Assertion
        assertThat(orderId1, Matchers.equalTo(first))

        //Second stage

        //Run
        val second = model?.activeOrderId?.getOrAwaitValue()

        //Assertion
        assertThat(second, Matchers.equalTo(first))
        assertThat(orderId2, Matchers.not(Matchers.equalTo(second)))

    }

    @Test
    fun checkThatRoleAndIsCheckedIsChangedWhen_setRole_IsInvoked() {

        //GIVEN
        val initRole = false

        //THEN
        assertThat(initRole, Matchers.equalTo(model!!.isChecked))    //PassengerRole = false

        //GIVEN
        val driverRole = true

        //WHEN
        model!!.changeRole(driverRole)

        //THEN
        assertThat(driverRole, Matchers.equalTo(model!!.isChecked))

    }


}