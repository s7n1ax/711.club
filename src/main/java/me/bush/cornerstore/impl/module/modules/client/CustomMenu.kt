package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.gui.menu.BetterMenu
import me.bush.cornerstore.impl.gui.menu.LarryScreen
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.render.Color
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.client.gui.GuiMainMenu
import net.minecraftforge.client.event.GuiOpenEvent

object CustomMenu : Module("Customize the Main Menu") {
    private val mode = ModeSetting("Theme", "Better", "Larry", "Club")
    private val background = ModeSetting("Background", "Default", "Shader", "Image")
    val noEdition = BooleanSetting("No \"Edition\"", false).withInfo("Removes \"Java Edition\" from the title")
    val noBranding = BooleanSetting("No Branding", true)
    val noCopyright = BooleanSetting("No Copyright", true)
    val noRealms = BooleanSetting("No Realms", true)
    val noLanguage = BooleanSetting("No Lang Btn", true).withInfo("Removes the small language button")
    val noSplash = BooleanSetting("No Splash", false)
    val splashColor = ColorSetting("Splash Color", Color.YELLOW)
    private val splash = ParentSetting("Splash", noSplash, splashColor)
    private val tweaks = ParentSetting("Tweaks", noEdition, noBranding, noCopyright, noRealms, noLanguage, splash).withVis { mode.value == "Better" }

    @EventListener
    fun onGuiOpen() = listener<GuiOpenEvent> {
        if (it.gui is GuiMainMenu) {
            it.gui = when (mode.value) {
                "Better" -> BetterMenu()
                "Larry" -> LarryScreen()
                else -> null // tood
            }
        }
    }

    init {
        enabled = true
    }
}
