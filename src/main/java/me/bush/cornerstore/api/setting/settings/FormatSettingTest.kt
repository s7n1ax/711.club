package me.bush.cornerstore.api.setting.settings

import com.google.gson.JsonObject
import me.bush.cornerstore.util.minecraft.ChatFormat

/**
 * @author bush
 * @since 3/25/2022
 */
class FormatSettingTest(name: String) : ParentSetting(name) {
    private val color = ModeSetting("Color", *ChatFormat.COLORS).withFormat { ChatFormat.nameOf(it) }
    private val bold = BooleanSetting("Bold", false)
    private val cursed = BooleanSetting("Cursed", false)
    private val italic = BooleanSetting("Italic", false)
    private val underline = BooleanSetting("Underline", false)
    private val strikethrough = BooleanSetting("Strikethrough", false)

    val format: String
        get() = StringBuilder(color.value).apply {
            if (bold.value) append(ChatFormat.BOLD)
            if (cursed.value) append(ChatFormat.CURSED)
            if (italic.value) append(ChatFormat.ITALIC)
            if (underline.value) append(ChatFormat.UNDERLINE)
            if (strikethrough.value) append(ChatFormat.STRIKETHROUGH)
        }.toString()

    init {
        transient = false
        addChildren(color, bold, cursed, italic, underline, strikethrough)
    }

    override fun fromConfig(config: JsonObject) {
        childSettings.forEach { it.fromConfig(config.get(name).asJsonObject) }
    }

    override fun toConfig(config: JsonObject) {
        config.add(name, JsonObject().also {
            childSettings.forEach { setting -> setting.toConfig(it) }
        })
    }
}
