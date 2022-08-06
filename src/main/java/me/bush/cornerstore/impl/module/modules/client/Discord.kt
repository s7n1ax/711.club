package me.bush.cornerstore.impl.module.modules.client

import club.minnced.discord.rpc.DiscordRPC
import club.minnced.discord.rpc.DiscordRichPresence
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.Background
import me.bush.cornerstore.util.minecraft.formattedCoords
import me.bush.cornerstore.util.minecraft.heldItemName
import kotlin.random.Random

/**
 * @author bush
 * @since 2/1/2022
 */
object Discord : Module("Manage Discord RPC") {
    private val showIp = BooleanSetting("Show IP", true)
    private val details = ModeSetting("Details", "IGN", "Coords", "Item", "Custom")
    private val catmode = BooleanSetting("Cat Mode", false)
    private val custom = StringSetting("Custom", "getting ratted by bush").withVis { details.value == "Custom" }
        .withInfo("Will also be displayed when mode is Item or Coords and you are not ingame")

    private var lastStatus = getStatus()
    private val presence = DiscordRichPresence().apply {
        largeImageText = CornerStore.NAME + " " + CornerStore.VERSION
        startTimestamp = System.currentTimeMillis()
        partySize = 0
        partyMax = 0
    }

    private fun getRandomImage() = if (catmode.value) "cat" + Random.nextInt(4) else "club" + Random.nextInt(5)

    private fun getStatus() = when {
        mc.world == null -> "In the Menu"
        mc.isSingleplayer -> "Playing Singleplayer"
        showIp.value -> "Playing on ${mc.currentServerData?.serverIP}"
        else -> "Playing Multiplayer"
    }

    private fun getDetails() = when (details.value) {
        "IGN" -> mc.session.username
        "Coords" -> mc.player?.let { "At " + it.formattedCoords } ?: custom.value
        "Item" -> mc.player?.let { "Holding " + it.heldItemName } ?: custom.value
        else -> custom.value
    }

    init {
        enabled = true
    }

    override fun onEnable() {
        Background.launch {
            while (enabled) {
                val status = getStatus()
                DiscordRPC.INSTANCE.Discord_UpdatePresence(presence.apply {
                    if (status != lastStatus) startTimestamp = System.currentTimeMillis()
                    details = getDetails()
                    state = status
                    largeImageKey = getRandomImage()
                })
                lastStatus = status
                // Discord only updates every 15 seconds
                delay(15_000)
            }
        }
        DiscordRPC.INSTANCE.Discord_Initialize("970199321662349362", null, true, null)
    }

    override fun onDisable() = DiscordRPC.INSTANCE.Discord_Shutdown()
}
