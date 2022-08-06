package me.bush.cornerstore.util.system

import me.bush.cornerstore.api.common.mc
import net.minecraft.network.Packet
import java.net.HttpURLConnection
import java.net.URL
import kotlin.streams.toList

/**
 * @author perry + bush.
 * @since 1/24/2022.
 */
object NetworkUtil {

    /**
     * Instantly sends the packet to the server. This method does not flag any packet events,
     * and skips any existing packet queue. May not be thread safe. Only use for packets that
     * are crucial to be dispatched as fast as possible, or those that shouldn't flag an event.
     */
    fun sendPacketInstantly(packet: Packet<*>?) {
        mc.connection?.networkManager?.channel()?.writeAndFlush(packet)
    }

    fun getAsciiText(text: String, style: String? = null): List<String> {
        val string = text.replace(" ", "+")
        val font = style ?: getLinesFromUrl("https://artii.herokuapp.com/fonts_list").random()
        return getLinesFromUrl("https://artii.herokuapp.com/make?text=$string&font=$font")
    }

    fun getLinesFromUrl(url: String): List<String> {
        val connection = URL(url).openConnection() as HttpURLConnection
        return connection.inputStream.bufferedReader().lines().toList()
    }
}
