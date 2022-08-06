package me.bush.cornerstore.api.gui.component

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.config.entry.SubConfig
import me.bush.cornerstore.api.gui.Component
import me.bush.cornerstore.api.gui.Theme
import me.bush.cornerstore.impl.module.modules.client.ClickGui
import me.bush.cornerstore.util.math.Vector
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.init.SoundEvents

/**
 * Simple component base for clickgui/hud.
 *
 * @author bush
 * @since 12/24/2021
 */
abstract class AbstractComponent(
    private val visibility: () -> Boolean = { true },
    override var name: String = "Component"
) : SubConfig, Component {
    val theme get() = Theme.current
    override val visible get() = visibility()
    override var tooltip: String? = null
    override val vec = Vector()

    protected fun playClick() {
        if (ClickGui.sounds.value) mc.soundHandler.playSound(
            PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1f)
        )
    }

    override fun fromJson(config: JsonObject) {
        config.get(name)?.asJsonArray?.let {
            vec.x = it[0].asFloat
            vec.y = it[1].asFloat
        }
    }

    override fun toJson(config: JsonObject) {
        config.add(name, JsonArray().apply {
            add(vec.x)
            add(vec.y)
        })
    }
}
