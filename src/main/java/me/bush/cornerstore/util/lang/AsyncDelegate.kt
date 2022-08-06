package me.bush.cornerstore.util.lang

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Runs the given block on another thread. If the delegated
 * property is called before the thread finishes, the
 * calling thread will be paused until it finishes.
 *
 * @author bush
 * @since 2/17/2022
 */
class AsyncDelegate<T>(block: suspend () -> T) : ReadWriteProperty<Any?, T> {
    private val deferred = Background.async { block() }
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null) runBlocking { value = deferred.await() }
        return value!!
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

fun <T> async(block: suspend () -> T) = AsyncDelegate(block)
