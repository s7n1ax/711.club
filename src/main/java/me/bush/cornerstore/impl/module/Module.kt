package me.bush.cornerstore.impl.module

import me.bush.cornerstore.api.common.Feature
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.modules.client.Settings
import me.bush.cornerstore.util.lang.properNoun
import me.bush.cornerstore.util.minecraft.ChatUtil
import me.bush.cornerstore.util.minecraft.Format.*
import me.bush.cornerstore.util.system.getClosestPackageName
import org.lwjgl.input.Keyboard
import kotlin.random.Random

/**
 * @author bush
 * @since fall 2021
 */
open class Module(description: String) : Feature(description) {
    // Default settings, under the preferences tab
    private val bindSetting = BindSetting("KeyBind", Keyboard.KEY_NONE).withVis { toggleable }
    private val bindMode = ModeSetting("Bind Mode", "Toggle", "Hold").withVis { toggleable }
    private val arrayDrawn = BooleanSetting("Array Drawn", true).withVis { toggleable || enabled }
    private val arrayColor = ColorSetting("Array Color", 255, 255, 255, 255).withVis { toggleable || enabled }.withInfo("Color of this module's name on the arraylist")
    private val toggleMsgs = BooleanSetting("Toggle Msgs", true).withVis { toggleable }
    // Every module has a unique chat message id, so toggle messages can be deleted
    private val toggleMsgsId = Random.nextInt()
    private val chatName get() = "${Settings.moduleColor.value}$BOLD$name$GRAY: $RESET"
    // Category this module is in, currently set through checking the package name
    final override val category get() = this::class.getClosestPackageName()!!.properNoun()
    open val displayInfo: String? = null

    var bind
        get() = bindSetting.value
        set(value) {
            bindSetting.value = value
        }

    init {
        preferences.addChildren(bindSetting, bindMode, arrayDrawn, arrayColor, toggleMsgs)
    }

    final override fun onToggle() {
        if (!toggleMsgs.value) return
        val message = if (enabled) "${GREEN}Enabled" else "${RED}Disabled"
        message(message, id = toggleMsgsId)
    }

    fun keyPressed(pressed: Boolean) {
        if (toggleable && (pressed || bindMode.isValue("Hold"))) toggle()
    }

    fun message(message: String, value: Any? = null, id: Int = 0) {
        ChatUtil.message(chatName + ChatUtil.format(message, value.toString()), id)
    }
}
