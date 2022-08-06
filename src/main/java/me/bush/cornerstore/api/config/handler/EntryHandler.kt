package me.bush.cornerstore.api.config.handler

import me.bush.cornerstore.api.config.entry.ConfigEntry

/**
 * @author bush
 * @since 2/26/2022
 */
class EntryHandler(val entry: ConfigEntry) : ConfigHandler {

    override fun load() {
//        logExceptions("There was an error while loading config for $entry", false) {
//            getPath(manager.current).let {
//                if (it.exists) {
//                    entry.fromJson(it.toJsonObject())
//                }
//            }
//        }
    }

    override fun save() {
//        logExceptions("There was an error while saving config for $entry") {
//            if (manager.isGlobal(entry)) {
//                manager.configs.forEach { save(it) }
//            } else save(manager.current)
//        }
    }

    private fun save(profile: String) {
        //entry.toJson().saveToFile(getPath(profile))
    }

    //private fun getPath(profile: String) = manager.directory.resolve(profile).resolve(entry.path)
}
