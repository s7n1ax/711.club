package me.bush.cornerstore.api.gui.component.slider

import me.bush.cornerstore.util.math.MathUtil

/**
 * A simple class that handles input/output for slider components. This class
 * solves the problem of having lots of duplicate code in the slider component
 * by handling all the input/output in another class, with a few abstract
 * methods for things such as parsing text to the desired number type and
 * converting the sliders 0-1 float output into the desired output number.
 *
 * @author bush
 * @since jan 2022
 */
abstract class SliderHandler<T : Number>(private val outputHook: (T) -> Unit, private val inputHook: () -> T) {
    val value get() = inputHook()
    val percent get() = percentFromValue(value)

    protected abstract fun valueFromPercent(percent: Float): T
    protected abstract fun percentFromValue(value: T): Float
    protected abstract fun parseText(text: String): T

    fun handleDrag(drag: Float) {
        outputHook(valueFromPercent(drag))
    }

    fun handleText(text: String) {
        runCatching { outputHook(parseText(text)) }
    }

    companion object {

        fun simpleFloatHandler(outputHook: (Float) -> Unit, inputHook: () -> Float, min: Float, max: Float): SliderHandler<Float> {
            return object : SliderHandler<Float>(outputHook, inputHook) {

                override fun valueFromPercent(percent: Float) = MathUtil.getValue(min, percent, max)

                override fun percentFromValue(value: Float) = MathUtil.getPercent(min, value, max)

                override fun parseText(text: String) = text.toFloat()
            }
        }

        fun simpleHandler(outputHook: (Float) -> Unit, inputHook: () -> Float): SliderHandler<Float> {
            return object : SliderHandler<Float>(outputHook, inputHook) {

                override fun valueFromPercent(percent: Float) = percent.coerceIn(0f, 1f)

                override fun percentFromValue(value: Float) = value.coerceIn(0f, 1f)

                override fun parseText(text: String) = text.toFloat()
            }
        }

        fun simpleIntHandler(outputHook: (Int) -> Unit, inputHook: () -> Int, min: Int, max: Int): SliderHandler<Int> {
            return object : SliderHandler<Int>(outputHook, inputHook) {

                override fun valueFromPercent(percent: Float) = MathUtil.getValue(min, percent, max)

                override fun percentFromValue(value: Int) = MathUtil.getPercent(min.toFloat(), value.toFloat(), max.toFloat())

                override fun parseText(text: String) = text.toInt()
            }
        }
    }
}
