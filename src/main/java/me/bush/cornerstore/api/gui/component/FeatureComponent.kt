package me.bush.cornerstore.api.gui.component

import me.bush.cornerstore.util.math.Vector

/**
 * A [ParentComponent] that can be toggled, opened and closed.
 *
 * @author bush
 * @since 12/22/2021
 */
class FeatureComponent(
    name: String,
    private val input: () -> Boolean = { true },
    private val output: (Boolean) -> Unit = {}
) : ParentComponent(name) {

    val enabled get() = input()

    override val headerHeight get() = theme.featureHeaderHeight
    override val footerHeight get() = theme.featureFooterHeight
    override val borderWidth get() = theme.featureBorderWidth

    override fun drawComponent() {
        theme.drawFeatureComponent(this)
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        if (isMouseOverHeader(mouse) && clicked && button == 0) {
            output(!input())
            playClick()
        }
        super.mouseClicked(mouse, button, clicked)
    }
}
