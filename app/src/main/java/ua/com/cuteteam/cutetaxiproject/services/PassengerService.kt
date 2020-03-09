package ua.com.cuteteam.cutetaxiproject.services

import android.content.Intent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import kotlin.coroutines.CoroutineContext

private const val ORDER_ID_NAME = "DriverService_orderId"

class PassengerService : BaseService(), CoroutineScope {

    private val dao by lazy {
        PassengerDao()
    }

    private val handler
        get() = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + handler

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY
    }

}