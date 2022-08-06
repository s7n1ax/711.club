package me.bush.cornerstore.api.config.entry

import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * Used for things that don't need their own [JsonObject],
 * and just add a [JsonElement] to the parent object.
 *
 * @author bush
 * @since 12/7/2021
 */
interface SubConfig {

    /**
     * Passes the same [JsonObject] that [toJson] added to.
     */
    fun fromJson(config: JsonObject)

    /**
     * Add properties to the given [JsonObject]
     */
    fun toJson(config: JsonObject)
}
