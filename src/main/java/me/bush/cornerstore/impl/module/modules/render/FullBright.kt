package me.bush.cornerstore.impl.module.modules.render

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.JoinEvent
import me.bush.cornerstore.api.event.events.ShutdownEvent
import me.bush.cornerstore.api.setting.onChange
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.FloatSetting
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.Module
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect


object FullBright : Module("makes everything bright") {
    private val potion = BooleanSetting("Potion", false).onChange(::updatePotion)
    private val gamma = BooleanSetting("Gamma", true).onChange(::updateGamma)
    private val gammaLevel = FloatSetting("Gamma Level", 30f, 0f, 30f).onChange { updateGamma(gamma.value) }.withVis(gamma::value)
    private var oldGamma = mc.gameSettings.gammaSetting

    override fun onEnable() {
        oldGamma = mc.gameSettings.gammaSetting
        updateGamma(gamma.value)
        updatePotion(potion.value)
    }

    @EventListener
    fun onJoin() = listener<JoinEvent> {
        updatePotion(potion.value)
    }

    override fun onDisable() {
        updatePotion(false)
        updateGamma(false)
        oldGamma = mc.gameSettings.gammaSetting
    }

    @EventListener
    fun onShutdown() = listener<ShutdownEvent> {
        if (gamma.value) {
            mc.gameSettings.let {
                it.gammaSetting = oldGamma
                // Save so gamma isn't fucked and our new
                // gamma isn't saved as the "old" gamma
                it.saveOptions()
            }
        }
    }

    private fun updateGamma(enabled: Boolean) {
        if (!this.enabled && enabled) return
        mc.gameSettings.gammaSetting = if (enabled) gammaLevel.value else oldGamma
    }

    private fun updatePotion(enabled: Boolean) {
        if ((!this.enabled && enabled)) return
        if (enabled) mc.player?.addPotionEffect(PotionEffect(MobEffects.NIGHT_VISION, Int.MAX_VALUE))
        else mc.player?.removePotionEffect(MobEffects.NIGHT_VISION)
    }
}
