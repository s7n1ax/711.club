package me.bush.cornerstore.api.event

import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.StagedEvent.Stage
import me.bush.cornerstore.api.event.events.*
import me.bush.eventbuskotlin.listener
import net.minecraft.client.gui.GuiGameOver
import net.minecraft.network.play.server.*
import net.minecraftforge.client.event.*
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.*
import org.lwjgl.input.Keyboard

/**
 * @author bush
 * @since 9/5/2021
 */
@Suppress("UNUSED_PARAMETER")
object EventTransformer : Manager {

    /**
     * ClientTickEvent   - Minecraft#runTick
     * PlayerTickEvent   - EntityPlayer#onUpdate
     * LivingUpdateEvent - EntityLivingBase#onUpdate
     * RenderTickEvent   - Minecraft#runGameLoop
     */
    override suspend fun primaryLoad() {
        MinecraftForge.EVENT_BUS.register(this)
        // https://wiki.vg/Entity_statuses#Egg (SPacketEntityStatus)
        @Suppress("UNNECESSARY_SAFE_CALL")
        EventBus.register(listener<PacketEvent.Receive>(parallel = true) { event ->
            // As of now nothing is time sensitive, we can do it on post process
            if (event.stage == Stage.POST) when (val packet = event.packet) {
                is SPacketRespawn -> if (dead) {
                    EventBus.post(RespawnEvent())
                    dead = false
                }
                is SPacketEntityStatus -> when (packet.opCode) {
                    3.toByte() -> packet.getEntity(mc.world)?.let { EventBus.post(DeathEvent(it)) }
                    35.toByte() -> packet.getEntity(mc.world)?.let { EventBus.post(DeathEvent(it)) }
                }
                is SPacketJoinGame -> EventBus.post(JoinEvent())
            }
        })
    }

    // 2 SPacketRespawns are sent, this makes sure we only post on the first one.
    var dead = false

    @SubscribeEvent(receiveCanceled = true)
    fun onGuiOpen(event: GuiOpenEvent) {
        EventBus.post(event)
        if (event.gui is GuiGameOver) {
            dead = true
            EventBus.post(DeathEvent(mc.player))
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onKeyInputInGame(event: KeyInputEvent) {
        EventBus.post(KeyEvent.InGame(Keyboard.getEventKey(), Keyboard.getEventKeyState(), Keyboard.isRepeatEvent()))
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onKeyInputInGui(event: KeyboardInputEvent) {
        EventBus.post(KeyEvent.InGui(Keyboard.getEventKey(), Keyboard.getEventKeyState(), Keyboard.isRepeatEvent()))
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onRenderOverlayPre(event: RenderGameOverlayEvent.Pre) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onRenderOverlayText(event: RenderGameOverlayEvent.Text) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onClientTick(event: ClientTickEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onPlayerTick(event: PlayerTickEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onRenderTick(event: RenderTickEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onPlayerDamaged(event: LivingDamageEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onChatSend(event: ClientChatEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onChatReceive(event: ClientChatReceivedEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onInputUpdate(event: InputUpdateEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onPushOutOfBlocks(event: PlayerSPPushOutOfBlocksEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onRenderBlockOverlay(event: RenderBlockOverlayEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onPlaySound(event: PlaySoundAtEntityEvent) {
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        EventBus.post(event)
    }
}
