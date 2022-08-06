package me.bush.cornerstore.impl.module.modules.client

import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.api.setting.onChange
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.gui.hud.HudEditorScreen
import me.bush.cornerstore.impl.gui.hud.HudManager
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.Background

/**
 * @author bush
 * @since fall 2021
 */
object Display : Module("Move stuff") {
    val render = BooleanSetting("Render", true).withInfo("Hud will only render when this is on").onChange { HudManager.shouldRender = it }
    val primaryText = ColorSetting("Prim. Text", 255, 255, 255, 255)
    val secondaryText = ColorSetting("Sec. Text", 200, 200, 200, 255)
    val resetComponents = ActionSetting("Reset Elements") { HudManager.resetPositions() }

    // Don't save open/close to config, or
    // hudeditor will open when we open mc
    override val transient = true

    override fun onEnable() {
        ClickGui.enabled = false
        mc.displayGuiScreen(HudEditorScreen)
    }

    override fun fromConfig(config: JsonObject) {
        // This doesn't save hud element positions,
        // just the position of the hud modules window
        Background.launch {
            HudEditorScreen.awaitPrimary().fromJson(config)
        }
    }

    override fun toConfig(config: JsonObject) {
        HudEditorScreen.toJson(config)
    }
}
