package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.bush.cornerstore.api.gui.component.special.ToggleComponent
import me.bush.cornerstore.api.setting.Setting

/**
 * @author bush
 * @since 12/16/2021
 */
open class BooleanSetting(name: String, value: Boolean) : Setting<Boolean>(name, value) {

    fun toggle() {
        value = !value
    }

    override val component
        get() = ToggleComponent(name, visibility, ::value)

    override fun fromConfig(config: JsonObject) {
        value = config.get(name).asBoolean
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonPrimitive(value))
    }
}
