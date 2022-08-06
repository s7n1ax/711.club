package me.bush.cornerstore.impl.module

import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.config.ConfigManager
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.KeyEvent
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.util.lang.logLoadTime
import me.bush.cornerstore.util.system.subclasses
import me.bush.eventbuskotlin.listener
import org.lwjgl.input.Keyboard

/**
 * @author bush
 * @since 9/5/2021
 */
object ModuleManager : Manager {
    val modules = mutableListOf<Module>()

    override suspend fun primaryLoad() {
        // Load modules
        logLoadTime("modules") {
            Module::class.subclasses
                .mapNotNull { it.objectInstance }
                .toCollection(modules)
            modules.sortBy { it.name }
            modules.size
        }
        // Register to ebent boos
        EventBus.register(listener<KeyEvent.InGame> { event ->
            // For some fucking reason my pcs volume keys are key_none, aka every modules bind
            if (!event.repeat && event.key != Keyboard.KEY_NONE) {
                modules.filter { it.bind == event.key }.forEach { it.keyPressed(event.pressed) }
            }
        })
        // Register configs
        ConfigManager.awaitPrimary().let {
            modules.forEach(it::register)
        }
    }
}
