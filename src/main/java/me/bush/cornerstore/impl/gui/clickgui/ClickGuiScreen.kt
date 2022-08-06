package me.bush.cornerstore.impl.gui.clickgui

import me.bush.cornerstore.api.gui.CornerstoreGui
import me.bush.cornerstore.api.gui.component.WindowComponent
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.impl.module.Category
import me.bush.cornerstore.impl.module.ModuleManager
import me.bush.cornerstore.impl.module.modules.client.ClickGui

/**
 * @author bush
 * @since 2/4/2022
 */
object ClickGuiScreen : CornerstoreGui() {
    override val bind get() = ClickGui.bind
    override val name get() = ClickGui.name

    override suspend fun loadGui() {
        ModuleManager.awaitPrimary()
        Category.names.forEach { category ->
            components += WindowComponent(category, false).apply {
                subComponents.addAll(ModuleManager.modules
                    .filter { it.category == category }.map { it.component })
            }
        }
    }

    override fun onClose() {
        ClickGui.enabled = false
    }
}
