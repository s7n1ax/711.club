package me.bush.cornerstore.impl.module.modules.movement

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.*
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.cancel
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.entity.MoverType
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent

/**
 * @author bush
 * @since 2/3/2022
 */
object Velocity : Module("Negates various types of knockback") {
    private val explosions = BooleanSetting("Explosions", true)
    private val knockback = BooleanSetting("Knockback", true)
    private val pistons = BooleanSetting("Pistons", true)
    private val blocks = BooleanSetting("Blocks", true)
    private val players = BooleanSetting("Players", true)
    private val liquids = BooleanSetting("Water", true)

    @EventListener
    fun onPacketRecieve() = listener<PacketEvent.Receive> { event ->
        if (event.stage == StagedEvent.Stage.POST) return@listener
        event.packet.let {
            if (it is SPacketEntityVelocity && it.entityID == mc.player?.entityId && knockback.value) event.cancel()
            else if (it is SPacketExplosion && explosions.value) event.cancel()
        }
    }

    @EventListener
    fun onPush() = listener<PushEvent> {
        if (it.type == PushEvent.Type.PLAYER && players.value) it.cancel()
        if (it.type == PushEvent.Type.LIQUID && liquids.value) it.cancel()
    }

    @EventListener
    fun onMove() = listener<MoveEvent> {
        if ((it.mover == MoverType.PISTON || it.mover == MoverType.SHULKER_BOX) && pistons.value) it.cancel()
    }

    @EventListener
    fun onBlockPush() = listener<PlayerSPPushOutOfBlocksEvent> {
        if (blocks.value) it.cancel()
    }
}
