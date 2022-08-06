package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.split
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraftforge.client.event.ClientChatEvent

/**
 * @author bush
 * @since fall 2021
 */
object ChatSuffix : Module("Appends a message to your chats") {
    private val custom = BooleanSetting("Custom", false)
    private val suffix = StringSetting("Custom Suffix", "| 711.CLUB").withVis { custom.value }
    private val suffixes = ModeSetting("Suffix", ": Sponsored by ${CornerStore.ALIAS}", ": bushmod", "meow").withVis { !custom.value }
    private val cmdPrefixes = StringSetting("Cmd Prefixes", "/ . - + @ ,").withInfo("Command prefixes that shouldn't have a suffix on them, seperated by a space")

    private val chatSuffix get() = if (custom.value) suffix.value else suffixes.value

    private val String.isValid
        get() = if (startsWith(Settings.commandPrefix.value)) false
        else cmdPrefixes.value.split().none(::startsWith)

    @EventListener
    fun onChatSend() = listener<ClientChatEvent> {
        if (it.message.isValid) {
            it.message = "${it.message} $chatSuffix"
        }
    }
}
