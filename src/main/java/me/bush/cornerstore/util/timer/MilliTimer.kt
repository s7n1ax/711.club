package me.bush.cornerstore.util.timer

/**
 * @author bush
 * @since 12/20/2021
 */
class MilliTimer : Timer() {
    private var time = System.currentTimeMillis()

    override fun reset() {
        time = System.currentTimeMillis()
    }

    override val elapsedNs get() = elapsedMs * 1_000_000L

    override val elapsedMs get() = (System.currentTimeMillis() - time).toInt()
}
