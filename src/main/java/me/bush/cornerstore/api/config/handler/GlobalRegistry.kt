package me.bush.cornerstore.api.config.handler

import com.google.gson.JsonArray
import me.bush.cornerstore.api.config.ConfigManager2
import me.bush.cornerstore.api.config.entry.ConfigEntry
import me.bush.cornerstore.util.lang.logExceptions
import me.bush.cornerstore.util.system.readJson
import me.bush.cornerstore.util.system.writeJson
import kotlin.io.path.name

/**
 * Manages which [ConfigEntry]s are globally saved/loaded.
 * A global config will have the same value, regardless of
 * the current configuration profile.
 *
 * @author bush
 * @since 2/25/2022
 */
class GlobalRegistry {
    private val path = ConfigManager2.path.resolve(name)
    private val registry = mutableListOf(*path.readJson().asJsonArray.map { it.asString }.toTypedArray())

    fun isGlobal(entry: ConfigEntry) = entry.path.name in registry

    fun setGlobal(entry: ConfigEntry, global: Boolean) {
        entry.path.name.let {
            if (global) registry += it else registry -= it
        }
        logExceptions("There was a problem while writing to $name") {
            path.writeJson(JsonArray().apply {
                registry.forEach { add(it) }
            })
        }
    }

//    override fun load() {
//        logExceptions("There was a problem while reading $globalRegistryJson") {
//            if (path.createFile()) {
//                path
//            }
//            registry.clear()
//            path.toJsonElement().asJsonArray.forEach {
//                registry += it.asString
//            }
//        }
//    }

    companion object {
        private const val name = "globalregistry.json"
    }
}
