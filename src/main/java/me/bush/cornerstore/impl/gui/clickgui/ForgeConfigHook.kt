package me.bush.cornerstore.impl.gui.clickgui

import me.bush.cornerstore.impl.module.modules.client.ClickGui
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory

/**
 * @author bush
 * @since jan 2022
 */
class ForgeConfigHook : IModGuiFactory {

    override fun initialize(mc: Minecraft) = Unit

    override fun hasConfigGui() = true

    override fun createConfigGui(parent: GuiScreen): GuiScreen {
        ClickGui.enabled = true
        return ClickGuiScreen
    }

    override fun runtimeGuiCategories() = null
}
