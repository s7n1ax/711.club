package me.bush.cornerstore.impl.module.modules.render

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.setting.onChange
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.ParentSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.cancel
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityBat
import net.minecraft.network.play.server.SPacketExplosion
import net.minecraftforge.client.event.RenderBlockOverlayEvent
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType.*
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent

/**
 * @author perry + bush
 * @since 11/12/2021.
 */
object NoRender : Module("Stops rendering of certain stuff.") {
    // Misc (via MixinWorld and MixinEntityRenderer)
    val hurtcam = BooleanSetting("Hurtcam", true).withInfo("Removes the hurt screen effect when taking damage.")
    val noweather = BooleanSetting("Weather", false).withInfo("Removes rain.")
    private val explosions = BooleanSetting("Explosions", true).withInfo("Removes explosion particles.")
    // Entities
    private val items = BooleanSetting("Items", false).withInfo("Removes items on the ground.").onChange { if (it) purgeEntities<EntityItem>() }
    private val bats = BooleanSetting("Bats", false).withInfo("Removes bats from the world.").onChange { if (it) purgeEntities<EntityBat>() }
    private val withers = BooleanSetting("Withers", false).onChange { if (it) purgeEntities<EntityWither>() }
    private val entities = ParentSetting("Entities", items, bats, withers)
    // Overlays
    private val helmet = BooleanSetting("Helmet", true)
    private val portal = BooleanSetting("Portal", true)
    private val bossbar = BooleanSetting("Bossbar", false)
    private val potions = BooleanSetting("Potion Icons", false)
    private val vignette = BooleanSetting("Vignette", false)
    private val fire = BooleanSetting("Fire", true)
    private val water = BooleanSetting("Water", true)
    private val block = BooleanSetting("Block", true)
    private val overlays = ParentSetting("Overlays", helmet, portal, bossbar, potions, vignette, fire, water, block)

    @EventListener
    fun onEntityJoinWorld() = listener<EntityJoinWorldEvent> {
        if ((it.entity is EntityItem && items.value) ||
            (it.entity is EntityBat && bats.value) ||
            (it.entity is EntityWither && withers.value)
        ) it.cancel()
    }

    @EventListener
    fun onPlaySound() = listener<PlaySoundAtEntityEvent> {
        val name = it.sound.soundName.path
        if ((bats.value && "bat" in name) ||
            // Excludes parrot imitate and wither skeleton
            (withers.value && "entity.wither." in name)
        ) it.cancel()
    }

    @EventListener
    fun onPacketReceive() = listener<PacketEvent.Receive> {
        if (it.packet is SPacketExplosion && explosions.value && it.stage == StagedEvent.Stage.POST) it.cancel()
    }

    @EventListener
    fun onGameOverlay() = listener<RenderGameOverlayEvent.Pre> {
        if ((it.type == HELMET && helmet.value) ||
            (it.type == PORTAL && portal.value) ||
            (it.type == BOSSINFO && bossbar.value) ||
            (it.type == POTION_ICONS && potions.value) ||
            (it.type == VIGNETTE && vignette.value)
        ) it.cancel()
    }

    @EventListener
    fun onBlockOverlay() = listener<RenderBlockOverlayEvent> {
        if ((it.overlayType == FIRE && fire.value) ||
            (it.overlayType == WATER && water.value) ||
            (it.overlayType == BLOCK && block.value)
        ) it.cancel()
    }

    private inline fun <reified T : Entity> purgeEntities() {
        mc.world?.loadedEntityList?.filterIsInstance<T>()?.forEach { it.setDead() }
    }
}
