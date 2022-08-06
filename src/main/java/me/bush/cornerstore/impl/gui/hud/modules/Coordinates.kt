package me.bush.cornerstore.impl.gui.hud.modules

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.impl.gui.hud.HudModule
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import kotlin.math.roundToInt

/**
 * @author bush
 * @since 3/5/2022
 */
class Coordinates : HudModule("10 kills on the board right now just wiped out tomato town", -2, 2) {
    private val decimals = BooleanSetting("Show Decimals", true)
    private val nether = BooleanSetting("Show Nether", true)
    private val xyz = BooleanSetting("Show \"XYZ\"", true)
    private var coords = getCoords()

    override val width get() = textWidth(coords)
    override val height get() = textHeight

    override fun drawScreen() = drawString(coords)

    @EventListener
    fun onTick() = listener<ClientTickEvent>(parallel = true) {
        if (it.phase == TickEvent.Phase.END) coords = getCoords()
    }

    // TODO: 3/6/2022 choose prim/sec colors with some indicator
    private fun getCoords() = mc.player?.let {
        StringBuilder().apply {
            if (xyz.value) append("XYZ ")
            append(format(it.posX)).append(", ")
            append(format(it.posY)).append(", ")
            append(format(it.posZ))
            if (nether.value) {
                val inNether = it.dimension == -1
                val netherX = if (inNether) it.posX / 8 else it.posX * 8
                val netherZ = if (inNether) it.posZ / 8 else it.posZ * 8
                append(" [")
                append(format(netherX)).append(", ")
                append(format(netherZ)).append("]")
            }
        }.toString()
    } ?: "[Coordinates]"

    private fun format(double: Double) = if (decimals.value) DF.format(double) else double.roundToInt().toString()
}
