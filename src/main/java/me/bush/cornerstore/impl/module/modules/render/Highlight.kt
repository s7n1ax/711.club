package me.bush.cornerstore.impl.module.modules.render

import me.bush.cornerstore.api.event.events.HighlightEvent
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.render.toRGBArray
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener

/**
 * @author bush
 * @since 3/8/2022
 */
object Highlight : Module("Customize your block highlight") {
    private val render = RenderSetting("Render")
    private val disable = BooleanSetting("Disable Highlight", false)
    private val offset = FloatSetting("Grow Offset", 0.2f, 0f, 1f)

    val rgba get() = render.outlineColor.value.hsba.toRGBArray()

    // TODO: 3/9/2022 redo with proper rendering
    @EventListener
    fun onBlockHighlight() = listener<HighlightEvent> {
        if (disable.value) it.cancel()
        it.red = rgba[0]
        it.green = rgba[1]
        it.blue = rgba[2]
        it.alpha = rgba[3]
        it.width = render.lineWidth.value
        it.grow = offset.value.toDouble() / 100
    }
}
