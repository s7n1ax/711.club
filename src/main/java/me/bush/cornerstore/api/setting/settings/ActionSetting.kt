package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import me.bush.cornerstore.api.gui.component.special.ActionComponent
import me.bush.cornerstore.api.setting.Setting

/**
 * @author bush
 * @since 12/16/2021
 */
class ActionSetting(name: String, action: Runnable) : Setting<Runnable>(name, action) {

    override val component get() = ActionComponent(name, visibility, value)

    init {
        // Will not be saved to config
        transient = true
    }

    override fun fromConfig(config: JsonObject) {}

    override fun toConfig(config: JsonObject) {}
}
