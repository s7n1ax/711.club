package me.bush.cornerstore.impl.managers

import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.util.timer.MilliTimer
import me.bush.cornerstore.util.timer.Timer

object SessionManager : Manager {
    private lateinit var timer: Timer
    val elapsedMs get() = timer.elapsedMs

    override suspend fun primaryLoad() {
        timer = MilliTimer()
    }
}
