package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.bush.cornerstore.api.gui.component.AbstractComponent
import me.bush.cornerstore.api.setting.Setting
import me.bush.cornerstore.util.math.Vector
import org.lwjgl.input.Keyboard

/**
 * @author bush
 * @since 12/16/2021
 */
class BindSetting(name: String, bind: Int) : Setting<Int>(name, bind) {

    override val component
        get() = object : AbstractComponent(visibility, name) {
            private var listening = false

            override fun drawScreen(mouse: Vector?) {
                val value = if (listening) "..." else Keyboard.getKeyName(value)
                theme.let {
                    vec.h = it.defaultHeaderHeight
                    it.drawValueComponent(this, value)
                }
            }

            override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
                if (clicked) {
                    if (isMouseOver(mouse) && button == 0) {
                        listening = !listening
                        playClick()
                    } else listening = false
                }
            }

            override fun keyTyped(typedChar: Char, keyCode: Int) {
                if (!listening) return
                when (keyCode) {
                    Keyboard.KEY_BACK, Keyboard.KEY_DELETE -> value = Keyboard.KEY_NONE
                    Keyboard.KEY_ESCAPE, Keyboard.KEY_RETURN -> {} // Do not change, just back out
                    else -> value = keyCode
                }
                listening = false
            }
        }

    override fun fromConfig(config: JsonObject) {
        value = Keyboard.getKeyIndex(config.get(name).asString)
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonPrimitive(Keyboard.getKeyName(value)))
    }
}
