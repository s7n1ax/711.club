package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.bush.cornerstore.api.gui.Component
import me.bush.cornerstore.api.gui.component.slider.SliderComponent
import me.bush.cornerstore.api.gui.component.slider.SliderHandler
import me.bush.cornerstore.api.setting.Setting

/**
 * @author bush
 * @since 2/17/2022
 */
class IntSetting(name: String, value: Int, val min: Int, val max: Int) : Setting<Int>(name, value) {
    override var value: Int
        get() = super.value
        set(value) {
            super.value = value.coerceIn(min, max)
        }

    override val component: Component
        get() {
            val handler = SliderHandler.simpleIntHandler({ value = it }, ::value, min, max)
            return SliderComponent(handler, visibility, name)
        }

    override fun fromConfig(config: JsonObject) {
        value = config.get(name).asInt
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonPrimitive(value))
    }
}
