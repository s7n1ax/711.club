package me.bush.cornerstore.util.math

/**
 * @author bush
 * @since fall 2021
 */
data class Vector(var x: Float = 0f, var y: Float = 0f, var w: Float = 0f, var h: Float = 0f) {

    inline infix fun with(block: Vector.() -> Unit): Vector {
        return copy().also(block)
    }

    operator fun plus(vec: Vector): Vector {
        return Vector(x + vec.x, y + vec.y, w + vec.w, h + vec.h)
    }

    operator fun minus(vec: Vector): Vector {
        return Vector(x - vec.x, y - vec.y, w - vec.w, h - vec.h)
    }

    fun isPointInside(vec: Vector) = MathUtil.isPointInside(this, vec)

    fun isPointInside(x: Int, y: Int) = MathUtil.isPointInside(this, x, y)

    fun shrink(amount: Float) {
        x += amount
        y += amount
        w -= amount * 2
        h -= amount * 2
    }

    fun expand(amount: Float) {
        x -= amount
        y -= amount
        w += amount * 2
        h += amount * 2
    }

    fun setOutofBounds() {
        x = -711f
        y = -711f
        w = -711f
        h = -711f
    }
}
