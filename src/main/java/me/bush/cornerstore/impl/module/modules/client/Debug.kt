package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.api.common.Logger
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.JoinEvent
import me.bush.cornerstore.api.setting.settings.ActionSetting
import me.bush.cornerstore.impl.gui.menu.LarryScreen
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.system.ReflectUtil
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener

/**
 * @author bush
 * @since 3/3/2022
 */
object Debug : Module("Random stuff") {
    private val testLarry = ActionSetting("Larry") { mc.displayGuiScreen(LarryScreen()) }
    private val eventBusInfo = ActionSetting("Bus Info", EventBus::debug)
    private val hardCrash = ActionSetting("Crash JVM") { ReflectUtil.UNSAFE.putAddress(0, 0) }
    private val softCrash = ActionSetting("Crash Minecraft") { throw RuntimeException("711 Debug Shutdown") }
    private val doGC = ActionSetting("Garbage Collection", System::gc)

    @EventListener
    fun onJoin() = listener<JoinEvent> {
        Logger.info("Joined game!")
    }
}
