package me.bush.cornerstore.util.render

import com.google.gson.JsonObject
import me.bush.cornerstore.api.common.Logger
import me.bush.cornerstore.api.config.entry.SubConfig
import me.bush.cornerstore.util.lang.logExceptions

/**
 * @author bush
 * @since 2/6/2022
 */
class Color(red: Int = 0, green: Int = 0, blue: Int = 0, alpha: Int = 255) : Cloneable, SubConfig {
    val observers = mutableListOf<(Color) -> Unit>()
    private var updating = false // Stops the update() function from recursing
    private val last = FloatArray(4) // Checked against hsba to determine if listeners should be notified of a change
    private val backup = FloatArray(4) // Save values before rainbow/variate/sync
    var speed = 1.0f // Variate/rainbow speed factor
    var range = 1.0f // Variate brightness range factor

    val hsba = ColorUtil.hsbaArrayOf(red, green, blue, alpha)
        get() {
            update()
            return field
        }

    var rainbow = false
        set(value) {
            if (field == value) return
            handleBackup(value, 0)
            field = value
        }

    var variate = false
        set(value) {
            if (field == value) return
            handleBackup(value, 1)
            field = value
        }

    val rgba get() = ColorUtil.getBits(hsba)

    var red
        get() = ColorUtil.getRed(rgba)
        set(value) {
            hsba.withRed(value / 255f)
        }

    var green
        get() = ColorUtil.getGreen(rgba)
        set(value) {
            hsba.withGreen(value / 255f)
        }

    var blue
        get() = ColorUtil.getBlue(rgba)
        set(value) {
            hsba.withBlue(value / 255f)
        }

    var alpha
        get() = ColorUtil.getAlpha(rgba)
        set(value) {
            hsba[3] = value / 255f
        }

    var hue
        get() = hsba[0]
        set(value) {
            hsba[0] = value
        }

    var saturation
        get() = hsba[1]
        set(value) {
            hsba[1] = value
        }

    var brightness
        get() = hsba[2]
        set(value) {
            hsba[2] = value
        }

    // Not entirely sure how well these offsets work, but it should be fine
    fun offsetHSBA(offset: Int) = hsba.copyOf().also {
        if (variate) it[2] = ColorUtil.getVariation(speed, range, backup[2], offset)
        if (rainbow) it[0] = ColorUtil.getRainbowHue(speed, offset)
    }

    fun offsetRGBA(offset: Int) = ColorUtil.getBits(offsetHSBA(offset))

    fun set(r: Int = red, g: Int = green, b: Int = blue, a: Int = alpha) {
        ColorUtil.hsbaArrayOf(r, g, b, a).copyInto(hsba)
    }

    fun set(h: Float = hsba[0], s: Float = hsba[1], b: Float = hsba[2], alpha: Float = hsba[3]) {
        floatArrayOf(h, s, b, alpha).copyInto(hsba)
    }

    fun setFrom(color: Color) {
        setFrom(color.hsba)
        color.backup.copyInto(backup)
        rainbow = color.rainbow
        variate = color.variate
        speed = color.speed
        range = color.range
    }

    fun setFrom(hsba: FloatArray) {
        hsba.copyInto(this.hsba)
    }

    inline infix fun with(block: Color.() -> Unit): Color {
        return clone().also(block)
    }

    public override fun clone(): Color {
        return Color(red, green, blue, alpha)
    }

    operator fun get(index: Int): Float {
        return hsba[index]
    }

    operator fun set(index: Int, value: Float) {
        hsba[index] = value
    }

    fun toHexString(): String {
        return "#" + String.format("%08X", rgba)
    }

    fun fromHexString(string: String): Boolean {
        val error = "Incorrect format! could not parse hex color"
        val hex = string.replace("#", "").replace("0x", "")
        if (hex.length == 6) {
            return logExceptions(error, false) {
                set(
                    Integer.valueOf(hex.substring(0, 2), 16),
                    Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16)
                )
                true
            }.getOrElse { false }
        }
        if (hex.length == 8) {
            return logExceptions(error, false) {
                set(
                    Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16),
                    Integer.valueOf(hex.substring(6, 8), 16),
                    Integer.valueOf(hex.substring(0, 2), 16)
                )
                true
            }.getOrElse { false }
        }
        Logger.error(error)
        return false
    }

    private fun update() {
        if (updating) return
        updating = true
        if (variate) hsba[2] = ColorUtil.getVariation(speed, range, backup[2])
        if (rainbow) hsba[0] = ColorUtil.getRainbowHue(speed)
        if (!last.contentEquals(hsba)) {
            observers.forEach { it(this) }
            hsba.copyInto(last)
        }
        updating = false
    }

    private fun handleBackup(start: Boolean, mode: Int) {
        if (start) hsba.copyInto(backup)
        else when (mode) {
            0 -> hue = backup[0]
            1 -> brightness = backup[2]
            // 2 -> setFrom(backup) // See line 12
        }
    }

    override fun fromJson(config: JsonObject) {
        // Rainbow and variate save defaults just fine, but for some fucking reason,
        // if you have both on, and load, the hue will default to what it was when
        // it loaded, not what it was before rainbow was enabled. I have better
        // things to spend my time on though. Or do I, if im writing paragraphs in
        // comments that nobody will ever read? Only time will tell....
        val red = config.get("red").asInt
        val green = config.get("green").asInt
        val blue = config.get("blue").asInt
        val alpha = config.get("alpha").asInt
        ColorUtil.hsbaArrayOf(red, green, blue, alpha)
            .copyInto(backup).copyInto(hsba)
        speed = config.get("speed").asFloat
        range = config.get("range").asFloat
        rainbow = config.get("rainbow").asBoolean
        variate = config.get("variate").asBoolean
    }

    override fun toJson(config: JsonObject) {
        config.apply {
            addProperty("speed", speed)
            addProperty("range", range)
            addProperty("rainbow", rainbow)
            addProperty("variate", variate)
            val oldRainbow = rainbow
            val oldVariate = variate
            if (!rainbow && !variate) {
                hsba.copyInto(backup)
            }
            rainbow = false
            variate = false
            addProperty("red", red)
            addProperty("green", green)
            addProperty("blue", blue)
            addProperty("alpha", alpha)
            rainbow = oldRainbow
            variate = oldVariate
        }
    }

    override fun toString(): String {
        return "RGBA[$red, $green, $blue, $alpha]"
    }

    companion object {
        var syncing: Color? = null

        val WHITE get() = Color(255, 255, 255)
        val BLACK get() = Color(0, 0, 0)
        val YELLOW get() = Color(255, 255, 0)
        val DEFAULT_BLOCK_HIGHLIGHT get() = Color(0, 0, 0, 102)
        val DEFAULT_OUTLINE get() = Color(160, 30, 200, 200)
        val DEFAULT_FILL get() = Color(160, 30, 200, 120)
    }
}
