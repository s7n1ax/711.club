package me.bush.cornerstore.impl.module.modules.render

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.impl.module.Module
import net.minecraft.client.settings.GameSettings

/**
 * MixinMinecraft
 *
 * @author bush
 * @since 3/2/2022
 */
object MoreFov : Module("Increases the range of the FOV slider") {

    override fun onEnable() {
        GameSettings.Options.FOV.run {
            valueMin = if (enabled) 1f else 30f
            valueMax = if (enabled) 180f else 110f
        }
    }

    override fun onDisable() {
        mc.gameSettings.let {
            it.fovSetting = it.fovSetting.coerceIn(30f, 110f)
        }
    }
}
