package me.bush.cornerstore.impl.module.modules.render

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.DeathEvent
import me.bush.cornerstore.api.event.events.PopEvent
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.IntSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.anynull
import me.bush.cornerstore.util.lang.mainThread
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.init.SoundEvents.ENTITY_LIGHTNING_IMPACT
import net.minecraft.init.SoundEvents.ENTITY_LIGHTNING_THUNDER
import net.minecraft.util.SoundCategory.MASTER
import kotlin.random.Random

/**
 * @author bush
 * @since 2/26/2022
 */
object Lightning : Module("Strikes people with lightning when they die/pop") {
    private val deaths = BooleanSetting("Deaths", true)
    private val pops = BooleanSetting("Pops", false)
    private val thunder = BooleanSetting("Thunder", true)
    private val range = IntSetting("Max Range", 30, 0, 150).withInfo("Set to 0 to disable range check")

    @EventListener
    fun onDeath() = listener<DeathEvent> { event ->
        if (event.entity != mc.player && deaths.value) smite(event.entity)
    }

    @EventListener
    fun onTotemPop() = listener<PopEvent> { event ->
        if (event.entity != mc.player && pops.value) smite(event.entity)
    }

    private fun smite(entity: Entity) {
        if (mc.anynull || (range.value != 0 && entity.getDistance(mc.player) > range.value)) return
        mainThread {
            // Has to be on main thread
            mc.world.spawnEntity(EntityLightningBolt(mc.world, entity.posX, entity.posY, entity.posZ, true))
        }
        if (thunder.value) mc.world.run {
            playSound(entity.position.up(5), ENTITY_LIGHTNING_THUNDER, MASTER, 10000f, 0.8f + Random.nextFloat() * 0.2f, false)
            playSound(entity.position.up(5), ENTITY_LIGHTNING_IMPACT, MASTER, 2f, 0.5f + Random.nextFloat() * 0.2f, false)
        }
    }
}
