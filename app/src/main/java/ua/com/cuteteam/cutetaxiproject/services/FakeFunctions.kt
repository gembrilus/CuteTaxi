package ua.com.cuteteam.cutetaxiproject.services

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import ua.com.cuteteam.cutetaxiproject.data.entities.Order

class FakeFunctions {

    private val functions = FirebaseFunctions.getInstance()

    fun fun1(order: Order): Task<Order>{
        val data = hashMapOf("order" to order)
        return functions
            .getHttpsCallable("")
            .call(data)
            .continueWith {
                (it.result?.data as Map<String, Any>)["order"] as Order
        }
    }


    fun fun2(order: Order): Task<Order>{
        val data = hashMapOf("order" to order)
        return functions
            .getHttpsCallable("")
            .call(data)
            .continueWith {
                (it.result?.data as Map<String, Any>)["order"] as Order
            }
    }

}