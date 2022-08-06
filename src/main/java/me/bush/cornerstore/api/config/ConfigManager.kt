package me.bush.cornerstore.api.config

import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.api.common.Logger
import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.config.entry.ConfigEntry
import me.bush.cornerstore.api.config.handler.ConfigHandler
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.ShutdownEvent
import me.bush.cornerstore.util.lang.logExceptions
import me.bush.cornerstore.util.system.*
import me.bush.eventbuskotlin.listener
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

/**
 * @author bush
 * @since 2/2/2022
 */
object ConfigManager : Manager {
    private val entries = mutableListOf<ConfigHandler>()
    private const val configJson = "CurrentConfig.json"
    val directory: Path = Paths.get(CornerStore.NAME)
    //private val globalRegistry by lazy { GlobalRegistry(this) }
    val configs get() = directory.subDirectories.map { it.name }
    var current: String
        get() = logExceptions("There was an error while accessing $configJson, or it may not exist. Defaulting to \"main\"", false) {
            ""//directory.resolve(configJson).readJson().get("Current").asString
        }.getOrElse {
            current = "main"
            "main"
        }
        set(value) {
            logExceptions("There was an error while saving $configJson") {
//                JsonObject().apply(fun JsonObject.() {
//                    addProperty("Current", value)
//                }).saveToFile(directory.resolve(configJson))
            }
        }

    override suspend fun primaryLoad() {
        return
        current
        if (configs.isEmpty()) {
            Logger.info("Possibly the first launch, creating config files. Welcome to 711.CLUB")
            logExceptions("There was an error while trying to create the default config directory") {
                directory.resolve("main").createPath()
            }
        }
        EventBus.register(listener<ShutdownEvent> {
            Logger.info("${CornerStore.ALIAS} saving configs..")
            saveAll()
            Logger.info("Configs saved.")
        })
    }

    override suspend fun secondaryLoad() {
        //loadAll()
        //saveAll()
    }

    fun register(entry: ConfigEntry) {
        //entries += EntryHandler(entry, this)
    }

    fun loadAll() {
        entries.forEach(ConfigHandler::load)
    }

    fun saveAll() {
        entries.forEach(ConfigHandler::save)
    }

    fun refresh() {
        current
        //globalRegistry.load()
    }

    fun delete(profile: String) {
        logExceptions("Could not delete config $profile") {
            directory.resolve(profile).deleteAll()
        }
    }

    fun isGlobal(entry: ConfigEntry) = false//globalRegistry.isGlobal(entry)

    fun setGlobal(entry: ConfigEntry, global: Boolean) {
        //globalRegistry.setGlobal(entry, global)
    }
}
