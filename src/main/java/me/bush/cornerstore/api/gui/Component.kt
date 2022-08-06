package me.bush.cornerstore.api.gui

import me.bush.cornerstore.api.common.Nameable
import me.bush.cornerstore.util.math.MathUtil
import me.bush.cornerstore.util.math.Vector

/**
 * @author bush
 * @since 3/5/2022
 */
interface Component : Nameable {
    override var name: String
    val visible: Boolean // make this less chinese
    val tooltip: String?
    val vec: Vector

    // TODO: 3/6/2022 onguiclose method to not fuck up keeping sliders clciked etc

    fun drawScreen(mouse: Vector?)

    fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean)

    fun keyTyped(typedChar: Char, keyCode: Int)

    fun isMouseOver(mouse: Vector) = MathUtil.isPointInside(vec with { shrink(1f) }, mouse)
}
