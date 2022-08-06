package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.bush.cornerstore.api.gui.component.AbstractComponent
import me.bush.cornerstore.api.setting.Setting
import me.bush.cornerstore.util.math.Vector

/**
 * @author bush
 * @since 12/16/2021
 */
open class ModeSetting<T>(name: String, vararg values: T) : Setting<T>(name, values[0]) {
    private val values = mutableListOf(*values)
    private var stringFormat: (T) -> String = { it.toString() }
    private var index = 0
    val stringValue get() = stringFormat(value)
    val size get() = values.size
    override var value: T
        get() = super.value
        set(value) {
            val index = values.indexOf(value)
            if (index == -1) return
            this.index = index
            super.value = value
        }

    fun withFormat(format: (T) -> String): ModeSetting<T> {
        stringFormat = format
        return this
    }

    fun setDefault(default: T): ModeSetting<T> {
        value = default
        return this
    }

    fun isValues(vararg values: T): Boolean {
        values.forEach {
            if (it == value) return true
        }
        return false
    }

    fun iterate() {
        if (index + 1 < values.size) index++ else index = 0
        value = values[index]
    }

    fun addValue(value: T) {
        if (value in values) return
        values += value
    }

    fun removeValue(value: T) {
        val index = values.indexOf(value)
        // Easier to require at least one element than to handle an empty list and null stuff
        if (index == -1 || values.size <= 1) return
        if (this.index == index) iterate()
        values.removeAt(index)
    }

    fun syncWith(newValues: List<T>) {
        if (newValues.isEmpty()) return
        newValues.forEach {
            if (it !in values) {
                addValue(it)
            }
        }
        values.forEach {
            if (it !in newValues) {
                removeValue(it)
            }
        }
        if (value !in newValues) {
            iterate()
        }
    }

    override val component
        get() = object : AbstractComponent(visibility, name) {

            override fun drawScreen(mouse: Vector?) {
                theme.let {
                    vec.h = it.defaultHeaderHeight
                    it.drawValueComponent(this, stringValue)
                }
            }

            override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
                if (isMouseOver(mouse) && clicked && button == 0) {
                    playClick()
                    iterate()
                }
            }

            override fun keyTyped(typedChar: Char, keyCode: Int) {}
        }

    override fun fromConfig(config: JsonObject) {
        val mode = config.get(name).asString
        value = values.firstOrNull { it.toString().substringBefore("@") == mode } ?: return
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonPrimitive(value.toString().substringBefore("@")))
    }
}
