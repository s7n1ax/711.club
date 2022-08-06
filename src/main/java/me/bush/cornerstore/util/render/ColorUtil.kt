package me.bush.cornerstore.util.render

import kotlin.math.*

/**
 * @author bush
 * @since jan 2022
 */
fun FloatArray.withRed(red: Float) {
    toRGBArray().also { it[0] = red }.toHSBArray().copyInto(this)
}

fun FloatArray.withGreen(green: Float) {
    toRGBArray().also { it[1] = green }.toHSBArray().copyInto(this)
}

fun FloatArray.withBlue(blue: Float) {
    toRGBArray().also { it[2] = blue }.toHSBArray().copyInto(this)
}

fun FloatArray.toRGBArray(): FloatArray {
    if (size < 4) throw ArrayIndexOutOfBoundsException("Cannot convert array with $size elements to rgba")
    var r = 0f
    var g = 0f
    var b = 0f
    if (this[1] == 0f) {
        b = this[2]
        g = b
        r = g
    } else {
        val h = (this[0] - floor(this[0])) * 6.0f
        val f = h - floor(h)
        val p = this[2] * (1.0f - this[1])
        val q = this[2] * (1.0f - this[1] * f)
        val t = this[2] * (1.0f - this[1] * (1.0f - f))
        when (h.toInt()) {
            0 -> {
                r = this[2]
                g = t
                b = p
            }
            1 -> {
                r = q
                g = this[2]
                b = p
            }
            2 -> {
                r = p
                g = this[2]
                b = t
            }
            3 -> {
                r = p
                g = q
                b = this[2]
            }
            4 -> {
                r = t
                g = p
                b = this[2]
            }
            5 -> {
                r = this[2]
                g = p
                b = q
            }
        }
    }
    return floatArrayOf(r, g, b, this[3])
}

fun FloatArray.toHSBArray(): FloatArray {
    if (size < 4) throw ArrayIndexOutOfBoundsException("Cannot convert array with $size elements to hsba")
    val s: Float
    val b: Float
    var h: Float
    var cmax = this[0].coerceAtLeast(this[1])
    if (this[2] > cmax) cmax = this[2]
    var cmin = this[0].coerceAtMost(this[1])
    if (this[2] < cmin) cmin = this[2]
    b = cmax
    s = if (cmax != 0f) (cmax - cmin) / cmax else 0f
    if (s == 0f) h = 0f else {
        val redc = (cmax - this[0]) / (cmax - cmin)
        val greenc = (cmax - this[1]) / (cmax - cmin)
        val bluec = (cmax - this[2]) / (cmax - cmin)
        h = if (this[0] == cmax) bluec - greenc else if (this[1] == cmax) 2.0f + redc - bluec else 4.0f + greenc - redc
        h /= 6.0f
        if (h < 0) h += 1.0f
    }
    return floatArrayOf(h, s, b, this[3])
}

object ColorUtil {

    fun getRainbowHue(speedFactor: Float, offset: Int = 0): Float {
        val speed = 2500 / speedFactor
        return ((System.currentTimeMillis() + offset) % speed.toInt()) / speed
    }

    fun getVariation(speedFactor: Float, rangeFactor: Float, value: Float, offset: Int = 0): Float {
        val time = sin(getRainbowHue(speedFactor / 36, offset) * 360)
        val variation = time * (rangeFactor / 8)
        return (value + variation).coerceIn(0f, 1f)
    }

    fun getBits(red: Int, green: Int, blue: Int, alpha: Int): Int {
        return ((alpha and 0xFF) shl 24) or ((red and 0xFF) shl 16) or ((green and 0xFF) shl 8) or ((blue and 0xFF) shl 0)
    }

    fun hsbaArrayOf(red: Int, green: Int, blue: Int, alpha: Int): FloatArray {
        return floatArrayOf(red / 255f, green / 255f, blue / 255f, alpha / 255f).toHSBArray()
    }

    fun getBits(hsba: FloatArray): Int {
        val rgba = hsba.toRGBArray()
        val red = (rgba[0] * 255).roundToInt()
        val green = (rgba[1] * 255).roundToInt()
        val blue = (rgba[2] * 255).roundToInt()
        val alpha = (rgba[3] * 255).roundToInt()
        return getBits(red, green, blue, alpha)
    }

    fun getRed(rgba: Int) = (rgba shr 16) and 0xFF

    fun getGreen(rgba: Int) = (rgba shr 8) and 0xFF

    fun getBlue(rgba: Int) = (rgba shr 0) and 0xFF

    fun getAlpha(rgba: Int) = (rgba shr 24) and 0xFF
}
