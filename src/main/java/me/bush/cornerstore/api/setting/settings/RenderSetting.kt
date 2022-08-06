package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.util.render.Color

/**
 * @author bush
 * @since 3/9/2022
 */
// TODO: 3/9/2022 shader option? other specific options with a toggle in the constr?
class RenderSetting(name: String) : ParentSetting(name) {
    val outline = BooleanSetting("Outline", true)
    val outlineColor = ColorSetting("Outline Color", Color.DEFAULT_OUTLINE).withVis { outline.value }
    val lineWidth = FloatSetting("Line Width", 1.0f, 0.1f, 5.0f).withVis { outline.value }
    val fill = BooleanSetting("Fill", true)
    val fillColor = ColorSetting("Fill Color", Color.DEFAULT_FILL).withVis { fill.value }

    init {
        addChildren(outline, outlineColor, lineWidth, fill, fillColor)
        transient = false
    }

    override fun fromConfig(config: JsonObject) {
        val configs = config.get(name).asJsonObject
        childSettings.forEach {
            it.fromJson(configs)
        }
    }

    override fun toConfig(config: JsonObject) {
        val configs = JsonObject()
        childSettings.forEach { it.toJson(configs) }
        config.add(name, configs)
    }
}
