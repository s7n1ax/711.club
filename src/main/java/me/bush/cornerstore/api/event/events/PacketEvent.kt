package me.bush.cornerstore.api.event.events

import me.bush.cornerstore.api.event.StagedEvent
import net.minecraft.network.Packet

/**
 * @author bush
 * @since 11/10/2021
 */
open class PacketEvent private constructor(val packet: Packet<*>, stage: Stage) : StagedEvent(stage) {

    class Send(packet: Packet<*>, stage: Stage) : PacketEvent(packet, stage)
    class Receive(packet: Packet<*>, stage: Stage) : PacketEvent(packet, stage)
}
