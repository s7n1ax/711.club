package me.bush.cornerstore.util.timer

/**
 * @author bush
 * @since 12/20/2021
 */
abstract class Timer {
    val elapsedSeconds get() = elapsedMs / 1_000
    val elapsedMsDecimal get() = elapsedNs / 1_000_000.0

    abstract val elapsedMs: Int

    abstract val elapsedNs: Long

    abstract fun reset()

    fun passedSeconds(time: Int): Boolean {
        return elapsedSeconds > time
    }

    fun passedMs(time: Int): Boolean {
        return elapsedMs > time
    }

    fun passedNs(time: Long): Boolean {
        return elapsedNs > time
    }

    fun passedSecondsReset(time: Int) = if (passedSeconds(time)) {
        reset()
        true
    } else false

    fun passedMsReset(time: Int) = if (passedMs(time)) {
        reset()
        true
    } else false

    // if(timer > 1500) is the same as timer.passedMs(1500)
    operator fun compareTo(time: Int): Int {
        return elapsedMs - time
    }
}
