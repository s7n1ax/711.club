package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.StringSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.*
import me.bush.cornerstore.util.minecraft.ChatFormat.GRAY
import me.bush.cornerstore.util.minecraft.rawClientMessage
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import me.bush.translator.*
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.client.event.ClientChatReceivedEvent

/**
 * @author bush
 * @since 5/25/2022
 */
object AutoTranslate : Module("Automatically translates incoming/outgoing chat messages") {
    private val incoming by BooleanSetting("Incoming", true) // todo: boolean parent settings
    private val suffix by BooleanSetting("Mark Translation", true)
        .withVis { incoming }.withInfo("Suffix translated messages with \"[Translated]\"")
    private val incomingLang by StringSetting("In Lang", "English").withVis { incoming }
    private val outgoing by BooleanSetting("Outgoing", false)
    private val outgoingLang by StringSetting("Out Lang", "Albanian").withVis { outgoing }

    private val translator = Translator()


    @EventListener // Min priority because we are cancelling the message event.
    fun onChatReceived() = listener<ClientChatReceivedEvent>(priority = Int.MIN_VALUE) { event ->
        if (!incoming) return@listener
        val language = getLanguage(incomingLang) ?: return@listener
        event.cancel()
        backgroundThread {
            translate(event.message.unformattedText, language) {
                mainThread {
                    val messageSuffix = if (suffix && sourceLanguage != targetLanguage) "$GRAY [Translated]" else ""
                    mc.rawClientMessage(translatedText + messageSuffix)
                }
            }
        }
    }

    @EventListener // ^^
    fun onChatSend() = listener<ClientChatEvent>(priority = Int.MIN_VALUE) { event ->
        if (!outgoing) return@listener
        // todo chat prefix manager thingy
        if (event.message.startsWith("/")) return@listener
        val language = getLanguage(outgoingLang) ?: return@listener
        event.cancel()
        backgroundThread {
            translate(event.message, language) {
                mainThread {
                    mc.player?.sendChatMessage(translatedText)
                }
            }
        }
    }

    private suspend inline fun translate(text: String, language: Language, block: Translation.() -> Unit) {
        translator.translateCatching(text, language).onFailure {
            message("Could not process translation request. Disabling")
            toggle()
        }.getOrNull()?.run(block)
    }

    private fun getLanguage(language: String) = languageOf(language).also {
        if (it == null) {
            //todo: module scope error msgs
            message("\"$language\" is not a valid language! Disabling.")
            toggle()
        }
    }
}
