package me.bush.cornerstore.mixin

import io.netty.channel.ChannelHandlerContext
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.StagedEvent.Stage.POST
import me.bush.cornerstore.api.event.StagedEvent.Stage.PRE
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author bush
 * @since 11/12/2021
 */
@Mixin(NetworkManager::class)
class MixinNetworkManager {

    /**
     * Packet send pre
     */
    @Inject(method = ["sendPacket(Lnet/minecraft/network/Packet;)V"], at = [At("HEAD")], cancellable = true)
    private fun onPacketSendPre(packet: Packet<*>, callback: CallbackInfo) {
        if (EventBus.post(PacketEvent.Send(packet, PRE))) callback()
    }

    /**
     * Packet send post
     */
    @Inject(method = ["sendPacket(Lnet/minecraft/network/Packet;)V"], at = [At("RETURN")])
    private fun onPacketSendPost(packet: Packet<*>, callback: CallbackInfo) {
        EventBus.post(PacketEvent.Send(packet, POST))
    }

    /**
     * Packet receive pre
     */
    @Inject(method = ["channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V"], at = [At("HEAD")], cancellable = true)
    private fun onPacketRecievePre(context: ChannelHandlerContext, packet: Packet<*>, callback: CallbackInfo) {
        if (EventBus.post(PacketEvent.Receive(packet, PRE))) callback()
    }

    /**
     * Packet receive post
     */
    @Inject(method = ["channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V"], at = [At("RETURN")])
    private fun onPacketRecievePost(context: ChannelHandlerContext, packet: Packet<*>, callback: CallbackInfo) {
        EventBus.post(PacketEvent.Receive(packet, POST))
    }
}
