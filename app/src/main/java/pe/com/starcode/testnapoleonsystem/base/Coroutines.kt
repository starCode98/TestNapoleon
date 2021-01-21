package pe.com.starcode.testnapoleonsystem.base

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object Coroutines {

    fun <T : Any> iothenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            var data = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work()
            }.await()
            callback(data)
        }

}