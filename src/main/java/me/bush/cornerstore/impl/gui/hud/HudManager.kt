package me.bush.cornerstore.impl.gui.hud

import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.config.ConfigManager
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.util.lang.logLoadTime
import me.bush.cornerstore.util.system.subclasses
import me.bush.eventbuskotlin.listener
import net.minecraftforge.client.event.RenderGameOverlayEvent
import kotlin.reflect.full.createInstance

object HudManager : Manager {
    val hudModules = mutableListOf<HudModule>()
    var shouldRender = true

    override suspend fun primaryLoad() {
        // TODO: 5/4/2022 objectss
        logLoadTime("hudmodules") {
            HudModule::class.subclasses
                .map { it.createInstance() }
                .toCollection(hudModules)
            hudModules.sortBy { it.name }
            hudModules.size
        }
        EventBus.register(listener<RenderGameOverlayEvent.Text> {
            if (shouldRender && mc.currentScreen !is HudEditorScreen) {
                hudModules.forEach { it.drawScreen(null) }
            }
        })
        ConfigManager.awaitPrimary().let {
            hudModules.forEach(it::register)
        }
    }

    fun resetPositions() {
        hudModules.forEach { it.resetPos() }
    }
}
