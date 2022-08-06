package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import me.bush.cornerstore.api.gui.component.ParentComponent
import me.bush.cornerstore.api.setting.Setting
import me.bush.cornerstore.util.math.Vector

/**
 * @author bush
 * @since 2/17/2022
 */
open class ParentSetting(name: String, vararg settings: Setting<*>?) : Setting<Boolean>(name, false) {
    protected val childSettings = mutableListOf<Setting<*>>()
    val children get() = childSettings.size

    init {
        transient = true
        addChildren(*settings)
    }

    fun addChildren(vararg settings: Setting<*>?) {
        settings.forEach {
            // Do our own null safety so the error message is actually helpful
            it ?: throw NullPointerException("Child Settings must be initialized above setting $name so they load properly!")
            it.hasParent = true
            childSettings += it
        }
        childSettings.sortBy { it.order }
    }

    override val component
        get() = object : ParentComponent(name, visibility) {

            init {
                subComponents += childSettings.map { it.component }
            }

            override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
                super.mouseClicked(mouse, button, clicked)
                value = open
            }
        }

    override fun fromConfig(config: JsonObject) {}

    override fun toConfig(config: JsonObject) {}
}
