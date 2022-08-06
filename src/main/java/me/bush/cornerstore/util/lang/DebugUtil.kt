package me.bush.cornerstore.util.lang

import me.bush.cornerstore.api.common.Logger
import me.bush.cornerstore.api.common.mc
import kotlin.system.measureNanoTime

inline fun measureDecimalTime(block: () -> Unit): Float {
    return measureNanoTime(block) / 1_000_000f
}

/**
 * Identical to [runCatching], but it logs to our client logger, and we can specify an error message.
 */
inline fun <T> logExceptions(message: String, trace: Boolean = true, block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (throwable: Throwable) {
        if (trace) {
            Logger.error(message, throwable)
        } else Logger.error(message)
        Result.failure(throwable)
    }
}

inline fun logLoadTime(type: String, block: () -> Int) {
    var count = 0
    val time = measureDecimalTime {
        count = block()
    }
    Logger.info("$count $type loaded, took $time ms.")
}

inline fun <T> profilerSection(name: String, block: () -> T): T {
    mc.profiler.startSection(name)
    val value = block()
    mc.profiler.endSection()
    return value
}

val FUCK: Nothing get() = throw Error("FUCK")
val OFF: Nothing get() = throw Error("OFF")
