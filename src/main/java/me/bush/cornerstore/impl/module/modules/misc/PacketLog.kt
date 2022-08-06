package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.common.Logger
import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.split
import me.bush.cornerstore.util.system.allMemberProperties
import me.bush.cornerstore.util.system.handleCall
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.network.Packet
import kotlin.reflect.jvm.isAccessible

/**
 * @author bush
 * @since 3/19/2022
 */
object PacketLog : Module("Records sent/recieved packets to chat or log file") {
    private val send = BooleanSetting("Send", true)
    private val receive = BooleanSetting("Receive", false)
    private val packets = StringSetting("Packets", "").withInfo("A list of packets to include/exclude, seperated by whitespace")
    private val mode = ModeSetting("Mode", "Include", "Exclude")
    private val properties = BooleanSetting("Show Properties", true).withInfo("Sends the value of all the properties in the packet")

    @EventListener
    fun onPacketSend() = listener<PacketEvent.Send> {
        if (send.value) handle(it)
    }

    @EventListener
    fun onPacketReceive() = listener<PacketEvent.Receive> {
        if (receive.value) handle(it)
    }

    private fun handle(event: PacketEvent) {
        if (event.stage != StagedEvent.Stage.PRE && valid(event.packet)) {
            Logger.info(event.packet::class.simpleName!!)
            if (properties.value) event.packet::class.allMemberProperties.forEach {
                it.isAccessible = true
                val value = it.handleCall(event.packet) ?: -1
                Logger.info("${it.name}: {}", value.toString().substringAfter(value::class.simpleName!!))
            }
        }
    }

    private fun valid(packet: Packet<*>) = packets.value.split().any { packet::class.simpleName == it } xor mode.isValue("Exclude")
}
