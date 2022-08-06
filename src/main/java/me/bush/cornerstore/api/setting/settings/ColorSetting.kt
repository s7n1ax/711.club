package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import me.bush.cornerstore.api.gui.component.special.ColorPickerComponent
import me.bush.cornerstore.api.setting.Setting
import me.bush.cornerstore.util.lang.invokeAll
import me.bush.cornerstore.util.render.Color

/**
 * @author bush
 * @since 12/16/2021
 */
class ColorSetting(name: String, red: Int, green: Int, blue: Int, alpha: Int) : Setting<Color>(name, Color(red, green, blue, alpha)) {
    val rgba get() = value.rgba

    // TODO: 3/9/2022 make this the main one
    constructor(name: String, color: Color) : this(name, color.red, color.green, color.blue, color.alpha)

    init {
        // Sync color observers with setting observers
        value.observers += observers::invokeAll
    }

    override val component get() = ColorPickerComponent(name, visibility, value)

    override fun fromConfig(config: JsonObject) {
        value.fromJson(config.get(name).asJsonObject)
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonObject().also { value.toJson(it) })
    }
}
