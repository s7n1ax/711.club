package me.bush.cornerstore.util.math

import kotlin.math.pow
import kotlin.math.sqrt


/**
 * @author bush
 * @since fall 2021
 */
object MathUtil {

    fun isPointInside(v: Vector, p: Vector): Boolean {
        return p.x >= v.x && p.x <= v.x + v.w && p.y >= v.y && p.y <= v.y + v.h
    }

    fun isPointInside(v: Vector, x: Int, y: Int): Boolean {
        return x >= v.x && x <= v.x + v.w && y >= v.y && y <= v.y + v.h
    }
    /**
     * Returns a number between 0 and 1 based off the input value and it's given range.
     */
    fun getPercent(min: Float, value: Float, max: Float): Float {
        return ((value - min) / (max - min)).coerceIn(0f, 1f)
    }
    /**
     * Returns a number within the given range corresinyonding to the input percent.
     */
    fun getValue(min: Int, percent: Float, max: Int): Int {
        return (min + (max - min) * percent).toInt().coerceIn(min, max)
    }
    /**
     * Returns a number within the given range corresinyonding to the input percent.
     */
    fun getValue(min: Float, percent: Float, max: Float): Float {
        return (min + (max - min) * percent).coerceIn(min, max)
    }
}

fun radians(degrees: Float) = Math.toRadians(degrees.toDouble())

fun pythag(x: Double, y: Double) = sqrt(x.pow(2) + y.pow(2))

fun DoubleArray.left90(): DoubleArray {
    val y = this[1]
    this[1] = this[0]
    this[0] = -this[1]
    return this
}

fun DoubleArray.right90(): DoubleArray {
    val y = this[1]
    this[1] = -this[0]
    this[0] = this[1]
    return this
}
