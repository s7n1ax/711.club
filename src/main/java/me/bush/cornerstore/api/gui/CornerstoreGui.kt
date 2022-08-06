package me.bush.cornerstore.api.gui

import com.google.gson.JsonObject
import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.common.Nameable
import me.bush.cornerstore.api.config.entry.SubConfig
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.gui.component.WindowComponent
import me.bush.cornerstore.impl.module.Category
import me.bush.cornerstore.impl.module.modules.client.ClickGui
import me.bush.cornerstore.util.lang.cancel
import me.bush.cornerstore.util.math.Vector
import me.bush.cornerstore.util.render.Render2DUtil
import me.bush.eventbuskotlin.listener
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

/**
 * Further abstraction may be added in the future, but for now this works.
 *
 * @author bush
 * @since 3/4/2022
 */
abstract class CornerstoreGui : GuiScreen(), Manager, SubConfig, Nameable {
    val components = mutableListOf<Component>()
    private val scrollMap = mutableMapOf<Component, Int>()

    abstract val bind: Int

    init {
        mc = Minecraft.getMinecraft()
        EventBus.register(listener<RenderGameOverlayEvent.Pre> {
            // Overlays to ignore
            if (ClickGui.hotbar.value && this == mc.currentScreen
                // Hides hud stuff from most clients, depending on when they render it
                && it.type != RenderGameOverlayEvent.ElementType.ALL
                && it.type != RenderGameOverlayEvent.ElementType.HELMET
                && it.type != RenderGameOverlayEvent.ElementType.PORTAL
                && it.type != RenderGameOverlayEvent.ElementType.VIGNETTE
            ) it.cancel()
        })
    }

    /**
     * All loading should be done in here, including initializing components and adding them to [components].
     */
    abstract suspend fun loadGui()

    /**
     * This will be called every time the gui is opened.
     */
    override fun initGui() {}

    /**
     * Called when the gui is closed, due to [bind] being pressed,
     * esc being pressed when [ClickGui.closeOnEscape] is enabled,
     * or by another screen being displayed.
     */
    open fun onClose() {}

    override suspend fun primaryLoad() {
        loadGui()
        components.filterIsInstance<WindowComponent>().forEach {
            scrollMap[it] = 0
        }
        alignComponents()
    }

    fun alignComponents() {
        resetScroll()
        val componentWidth = ClickGui.width.value
        val categories = Category.names
        val spacing = if (ClickGui.alignMode.isValue("Center")) {
            (width - componentWidth * categories.size) / (categories.size + 1)
        } else 6
        var x = spacing
        components.filter { it is WindowComponent && !it.special }
            .sortedBy { categories.indexOf(it.name) }
            .forEach {
                it.vec.x = x.toFloat()
                it.vec.y = spacing.toFloat()
                x += spacing + componentWidth
            }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val mouse = Vector(mouseX.toFloat(), mouseY.toFloat())
        val globalScroll = ClickGui.globalScroll.value
        val scrollDelta = getScrollDelta()
        GlStateManager.pushMatrix()
        if (mc.world != null) drawBackGround() else {
            // So gui looks normal when opened through forge config screen
            GlStateManager.enableDepth()
            if (!ClickGui.mintMode.value) {
                // Avoids funny windows XP bug
                drawDefaultBackground()
            }
        }
        GlStateManager.translate(0f, 0f, 711f)
        components.forEach {
            if (it is WindowComponent) {
                // Set width for normal (module list) components
                if (!it.special) {
                    it.vec.w = ClickGui.width.value.toFloat()
                }
                // Update all components
                if (globalScroll) scrollComponent(it, scrollDelta)
            }
            it.drawScreen(mouse)
            // Only lets one component get mouse coords if multiple are under mouse
            if (it.isMouseOver(mouse)) {
                if (!globalScroll && it is WindowComponent) {
                    // Update only this component
                    scrollComponent(it, scrollDelta)
                }
                mouse.setOutofBounds()
            }
            // Avoid z fighting shit
            GlStateManager.translate(0f, 0f, -10f)
        }
        GlStateManager.popMatrix()
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == bind || (keyCode == Keyboard.KEY_ESCAPE && ClickGui.closeOnEscape.value)) {
            mc.displayGuiScreen(null)
            mc.setIngameFocus()
            onGuiClosed()
        }
        // Still send key to components (If bind was pressed, change to escape so text boxes, etc can close)
        components.forEach { it.keyTyped(typedChar, if (keyCode == bind) Keyboard.KEY_ESCAPE else keyCode) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        doMouse(Vector(mouseX.toFloat(), mouseY.toFloat()), button, true)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, button: Int) {
        doMouse(Vector(mouseX.toFloat(), mouseY.toFloat()), button, false)
    }

    override fun onGuiClosed() {
        if (ClickGui.backgroud.isValue("Blur") && mc.entityRenderer.isShaderActive) {
            mc.entityRenderer.stopUseShader()
        }
        if (ClickGui.resetScroll.value) resetScroll()
        onClose()
    }

    override fun doesGuiPauseGame() = false

    override fun fromJson(config: JsonObject) {
        resetScroll()
        components.filterIsInstance<WindowComponent>().forEach {
            it.fromJson(config.get(name)?.asJsonObject ?: return@forEach)
        }
    }

    override fun toJson(config: JsonObject) {
        resetScroll()
        config.add(name, JsonObject().apply {
            components.filterIsInstance<WindowComponent>().forEach {
                it.toJson(this)
            }
        })
    }

    private fun doMouse(mouse: Vector, button: Int, clicked: Boolean) {
        var toReorder: Component? = null
        // Sending click to every component, so they know when you click somewhere else
        components.forEach {
            it.mouseClicked(mouse, button, clicked)
            // If the click is over this category
            if (it.isMouseOver(mouse)) {
                if (clicked) {
                    // Avoid concurrent modification bs
                    toReorder = it
                }
                // Change mouse coordinates so only the top window receives click
                mouse.setOutofBounds()
            }
        }
        toReorder?.let {
            // Move to end of list
            components.remove(it)
            components.add(0, it)
        }
    }

    private fun drawBackGround() {
        // Maybe add custom shader mode?
        val mode = ClickGui.backgroud
        when (mode.value) {
            "Blur" -> if (!mc.entityRenderer.isShaderActive) {
                mc.entityRenderer.loadShader(ResourceLocation("shaders/post/blur.json"))
            }
            "Dirt" -> drawBackground(0)
            "Default" -> drawDefaultBackground()
            "Gradient" -> {
                val start = ClickGui.gradientStart.rgba
                val end = ClickGui.gradientEnd.rgba
                val vertical = ClickGui.vertical.value
                Render2DUtil.gradientFillRect(0f, 0f, width.toFloat(), height.toFloat(), 0f, start, end, vertical)
            }
        }
        if (!mode.isValue("Blur") && mc.entityRenderer.isShaderActive) {
            mc.entityRenderer.stopUseShader()
        }
    }

    private fun getScrollDelta(): Int {
        val multiplier = if (ClickGui.invertScroll.value) -0.1f else 0.1f
        return (Mouse.getDWheel() * ClickGui.scrollSpeed.value * multiplier).toInt()
    }

    private fun scrollComponent(component: Component, scrollDelta: Int) {
        scrollMap.computeIfPresent(component) { _, v -> v + scrollDelta }
        component.vec.y += scrollDelta.toFloat()
    }

    private fun resetScroll() {
        scrollMap.entries.forEach {
            it.key.vec.y -= it.value.toFloat()
            it.setValue(0)
        }
    }
}
