package ua.com.cuteteam.cutetaxiproject

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object TestCoroutineDispatcher : CoroutineDispatcher(){
    override fun dispatch(context: CoroutineContext, block: Runnable) = block.run()
}