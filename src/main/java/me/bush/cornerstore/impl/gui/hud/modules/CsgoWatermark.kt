package me.bush.cornerstore.impl.gui.hud.modules

import com.mojang.realmsclient.gui.ChatFormatting
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.api.setting.settings.ColorSetting
import me.bush.cornerstore.impl.gui.hud.HudModule
import me.bush.cornerstore.impl.module.modules.client.NameChanger
import me.bush.cornerstore.util.render.Color
import me.bush.cornerstore.util.render.Render2DUtil

/**
 * @author noat
 * @since 3/6/2022 (GMT+ 7 aka indochina time)
 */
class CsgoWatermark : HudModule("cooler watermark i guess lmao", 2, 2) {
    // 21 is from (7 * 3) which is the gap between each stuff so, I can render rectangle.
    // good grammar because intellij is retarded and is spamming me with this fucking green errors.
    val gradientStart = ColorSetting("Start Color", 121, 255, 63, 255)
    val gradientEnd = ColorSetting("End Color", 255, 127, 40, 255)
    override val width get() = textWidth("711.club" + mc.session.username) + 20
    override val height get() = textHeight + 4

    override fun drawScreen() {
        // draw original rectangle
        Render2DUtil.fillRect(vec, -1f, Color(0, 0, 0).rgba)
        // draw the inside rectangle
        Render2DUtil.fillRect(vec.x + 1, vec.y + 1, vec.w - 2, vec.h - 2, 0f, Color(35, 35, 35).rgba)
        // gradient
        Render2DUtil.gradientFillRect(vec.x, vec.y - 1, vec.w, 1f, 0f, gradientStart.rgba, gradientEnd.rgba, false)
        // text -
        // 711.club text
        FontRenderer.drawString("" + ChatFormatting.GREEN + "711" + ChatFormatting.RESET + ".CLUB", (vec.x + 4).toInt(), (vec.y + 3).toInt(), Color(255, 255, 255).rgba)
        // name
        FontRenderer.drawString(if (NameChanger.enabled) NameChanger.alias.value else mc.session.username, (vec.x + (vec.w - textWidth(if (NameChanger.enabled) NameChanger.alias.value else mc.session.username)) - 4).toInt(), (vec.y + 3).toInt(), Color(255, 255, 255).rgba)
        // ----------
        // 711.club rectangle
        Render2DUtil.fillRect(vec.x + textWidth("711.CLUB") + 7, vec.y + 3, 1f, textHeight.toFloat() - 2, 0f, Color(255, 255, 255).rgba)
        // username rectangle
//        Render2DUtil.fillRect(vec.x + textWidth(if (NameChanger.nabled) NameChanger.alias.value else mc.session.username) + 19, vec.y + 8f, 1f, textHeight.toFloat() - 1, 0f, Color(255, 255, 255).rgba)
    }
}
