package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.bush.cornerstore.api.gui.component.special.TextBoxComponent
import me.bush.cornerstore.api.setting.Setting

/**
 * @author bush
 * @since 12/16/2021
 */
class StringSetting(name: String, value: String) : Setting<String>(name, value) {

    override val component
        get() = TextBoxComponent(name, visibility, ::value) { value = it }

    override fun fromConfig(config: JsonObject) {
        value = config.get(name).asString
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonPrimitive(value))
    }
}
