package me.bush.cornerstore.api.config.entry

import com.google.gson.JsonObject
import java.nio.file.Path

/**
 * Config interface for top level configs, like modules. These have their own directory.
 *
 * @author bush
 * @since 2/21/2022
 */
interface ConfigEntry {

    /**
     * This should contain the relative path to this config.
     * For example, you could return any of these:
     * ```
     * Paths.get("modules/combat/module.json")
     * Paths.get("gui/clickgui.json")
     * ```
     */
    val path: Path

    /**
     * The [JsonObject] passed to this method is structurally
     * identical to the one returned from [toJson].
     */
    fun fromJson(config: JsonObject) {
    }

    /**
     * Top level configs have their own [JsonObject].
     */
    fun toJson(): JsonObject
}
