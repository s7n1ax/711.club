package me.bush.cornerstore.api.config

import kotlinx.coroutines.launch
import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.api.common.Logger
import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.config.entry.ConfigEntry
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.ShutdownEvent
import me.bush.cornerstore.util.lang.Background
import me.bush.cornerstore.util.lang.logExceptions
import me.bush.cornerstore.util.system.*
import me.bush.eventbuskotlin.listener
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * @author bush
 * @since 3/20/2022
 */
object ConfigManager2 : Manager {
    val path = Path(CornerStore.NAME)

    val entries = mutableListOf<ConfigEntry>()

    // _global or .global directory for glboal configs
    // how to save global state or global toggle?

    override suspend fun primaryLoad() {
        entries += ConfigEntry::class.subclasses.mapNotNull { it.objectInstance }
        EventBus.register(listener<ShutdownEvent> {
            Logger.info("Saving configs..")
            save()
            Logger.info("Configs saved.")
        })
    }

    override suspend fun secondaryLoad() {
        load()
        save()
    }

    fun load() {
        Background.launch {
            entries.forEach { entry ->
                logExceptions("There was an error while loading config for $entry", trace = false) {
                    subpath(entry.path).let {
                        if (it.exists) entry.fromJson(it.readJson().asJsonObject)
                    }
                }
            }
        }
    }

    fun save() {
        Background.launch {
            entries.forEach { entry ->
                logExceptions("There was an error while saving config for $entry") {
                    subpath(entry.path).let {
                        if (it.exists) it.writeJson(entry.toJson())
                    }
                }
            }
        }
    }

    private fun subpath(subpath: Path) = path.resolve("todo").resolve(subpath)


//    private const val configJson = "CurrentConfig.json"
//    private val entries = mutableListOf<ConfigHandler>()
//    val directory: Path = Paths.get(CornerStore.NAME)
//    val configs get() = directory.subDirectories.mapNotNull { if (it.name == "global") null else it.name }
//    var current: String
//        get() = logExceptions("There was an error while accessing $configJson, or it may not exist. Defaulting to \"main\"", false) {
//            directory.resolve(configJson).toJsonObject().get("Current").asString
//        }.getOrElse { "main".also { current = it } }
//        set(value) {
//            logExceptions("There was an error while saving $configJson") {
//                JsonObject().apply {
//                    addProperty("Current", value)
//                }.saveToFile(directory.resolve(configJson))
//            }
//        }
//
//    override suspend fun primaryLoad() {
//        current
//        if (configs.isEmpty()) {
//            Logger.info("Possibly the first launch, creating config files. Welcome to 711.CLUB")
//            logExceptions("There was an error while trying to create the default config directory") {
//                directory.resolve("main").createPath()
//            }
//        }
//        EventBus.register(listener<ShutdownEvent> {
//            Logger.info("${CornerStore.ALIAS} saving configs..")
//            saveAll()
//            Logger.info("Configs saved.")
//        })
//    }
//
//    override suspend fun secondaryLoad() {
//        loadAll()
//        saveAll()
//    }
//
//    fun register(entry: ConfigEntry) {
//        //entries += EntryHandler(entry, this)
//    }
//
//    fun loadAll() {
//        entries.forEach(ConfigHandler::load)
//    }
//
//    fun saveAll() {
//        entries.forEach(ConfigHandler::save)
//    }
//
//    fun refresh() {
//        current
//        loadAll()
//    }
//
//    fun delete(profile: String) {
//        // add checks here
//        logExceptions("Could not delete config $profile") {
//            directory.resolve(profile).deleteAll()
//        }
//    }
}
