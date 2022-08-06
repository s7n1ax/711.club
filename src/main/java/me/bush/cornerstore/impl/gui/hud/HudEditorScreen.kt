package me.bush.cornerstore.impl.gui.hud

import me.bush.cornerstore.api.gui.CornerstoreGui
import me.bush.cornerstore.api.gui.component.FeatureComponent
import me.bush.cornerstore.api.gui.component.WindowComponent
import me.bush.cornerstore.api.gui.component.special.ToggleComponent
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.impl.module.modules.client.Display

/**
 * @author bush
 * @since 3/4/2022
 */
object HudEditorScreen : CornerstoreGui() {
    override val bind get() = Display.bind
    override val name get() = Display.name

    var cope = false

    override suspend fun loadGui() {
        HudManager.awaitPrimary()
        components += WindowComponent("Display", false).apply {
            subComponents += FeatureComponent("Feature").apply {
                subComponents += FeatureComponent("SubFeature").apply {
                    subComponents += ToggleComponent("Thing", { true }, ::cope)
                }
            }
            subComponents += HudManager.hudModules.map { it.component }
        }
    }

    override fun initGui() {
        components.addAll(HudManager.hudModules)
    }

    override fun onClose() {
        // TODO: 5/6/2022 this better
        Display.enabled = false
        components.removeAll(HudManager.hudModules)
    }
}
