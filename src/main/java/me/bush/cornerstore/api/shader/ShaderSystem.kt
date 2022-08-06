package me.bush.cornerstore.api.shader

import com.google.gson.JsonObject
import me.bush.cornerstore.api.config.entry.SubConfig

/**
 * @author bush
 * @since 3/12/2022
 */
class ShaderSystem : SubConfig {



    init {

    }

    fun startUse() = Unit

    fun stopUse() = Unit

    override fun fromJson(config: JsonObject) {
        TODO("Not yet implemented")
    }

    override fun toJson(config: JsonObject) {
        TODO("Not yet implemented")
    }
}
