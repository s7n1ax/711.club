package me.bush.cornerstore.util.timer

/**
 * @author bush
 * @since 12/20/2021
 */
class NanoTimer : Timer() {
    private var time = System.nanoTime()

    override fun reset() {
        time = System.nanoTime()
    }

    override val elapsedMs get() = (elapsedNs / 1_000_000).toInt()

    override val elapsedNs get() = System.nanoTime() - time
}
