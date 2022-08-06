package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.bush.cornerstore.util.minecraft.Format

/**
 * @author bush
 * @since 2/15/2022
 */
class FormatSetting(name: String, default: Format = Format.WHITE) : ModeSetting<Format>(name, *Format.colors) {
    val format get() = value.toString()
    // TODO: 3/25/2022 add support for all formats
    init {
        withFormat { it.name }
        setDefault(default)
    }

    override fun fromConfig(config: JsonObject) {
        value = Format.valueOf(config.get(name).asString)
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonPrimitive(value.name))
    }
}
