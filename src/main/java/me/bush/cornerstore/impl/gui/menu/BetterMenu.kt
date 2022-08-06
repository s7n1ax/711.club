package me.bush.cornerstore.impl.gui.menu

import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.impl.gui.clickgui.ClickGuiScreen
import me.bush.cornerstore.impl.module.modules.client.CustomMenu
import me.bush.cornerstore.util.minecraft.SplashUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.resources.I18n
import net.minecraft.realms.RealmsBridge
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.gui.NotificationModUpdateScreen
import net.minecraftforge.fml.client.GuiModList
import net.minecraftforge.fml.common.FMLCommonHandler
import org.lwjgl.input.Mouse
import org.lwjgl.util.glu.Project
import kotlin.random.Random

/**
 * @author bush
 * @since 3/17/2022
 */
class BetterMenu : GuiScreen() {
    private var minceraftRoll = 0f
    private lateinit var splashText: String
    private var panoramaTimer = 0f
    private lateinit var backgroundTexture: ResourceLocation
    private lateinit var modButton: GuiButton
    private lateinit var modUpdateNotification: NotificationModUpdateScreen
    private lateinit var realmsButton: GuiButton
    private var checkedRealms = false
    private var realmsScreen: GuiScreen? = null

    private val copyrightWidth get() = width - COPYRIGHT_WIDTH - 2 // TODO: 3/20/2022

    override fun initGui() {
        backgroundTexture = mc.textureManager.getDynamicTextureLocation("background", DynamicTexture(256, 256))
        splashText = SplashUtil.getRandomSplash()
        minceraftRoll = Random.nextFloat()
        initButtons()
        initRealmsAndUpdate()
    }

    private fun initButtons() {
        val spacing = height / 4 + 48
        addButton(GuiButton(1, width / 2 - 100, spacing, I18n.format("menu.singleplayer")))
        addButton(GuiButton(2, width / 2 - 100, spacing + 24, I18n.format("menu.multiplayer")))
        if (!CustomMenu.noRealms.value) {
            realmsButton = addButton(GuiButton(14, width / 2 + 2, spacing + 24 * 2, 98, 20, I18n.format("menu.online").replace("Minecraft", "").trim()))
        } else addButton(GuiButton(711, width / 2 + 2, spacing + 48, 98, 20, "ClickGui"))
        modButton = addButton(GuiButton(6, width / 2 - 100, spacing + 24 * 2, 98, 20, I18n.format("fml.menu.mods")))
        addButton(GuiButton(0, width / 2 - 100, spacing + 84, 98, 20, I18n.format("menu.options")))
        addButton(GuiButton(4, width / 2 + 2, spacing + 84, 98, 20, I18n.format("menu.quit")))
        if (!CustomMenu.noLanguage.value) addButton(GuiButtonLanguage(5, width / 2 - 124, spacing + 84))
    }

    private fun initRealmsAndUpdate() {
        mc.isConnectedToRealms = false
        if (!CustomMenu.noRealms.value) {
            if (mc.gameSettings.realmsNotifications && !checkedRealms) {
                realmsScreen = RealmsBridge().getNotificationScreen(this)
                checkedRealms = true
            }
            if (mc.gameSettings.realmsNotifications) realmsScreen?.let {
                it.setGuiSize(width, height)
                it.initGui()
            }
        }
        modUpdateNotification = NotificationModUpdateScreen(modButton).also {
            it.setGuiSize(width, height)
            it.initGui()
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        renderSkybox(partialTicks)
        // cancel gradient if shaders
        drawGradientRect(0, 0, width, height, -2130706433, 16777215)
        drawGradientRect(0, 0, width, height, 0, Int.MIN_VALUE)
        drawTitle()
        if (!CustomMenu.noSplash.value) drawSplash()
        if (!CustomMenu.noBranding.value) drawBrandings()
        if (!CustomMenu.noCopyright.value) drawCopyright(mouseX, mouseY)
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawRealmsAndUpdate(mouseX, mouseY, partialTicks)
    }

    private fun drawRealmsAndUpdate(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (!CustomMenu.noRealms.value && mc.gameSettings.realmsNotifications && realmsScreen != null) {
            realmsScreen!!.drawScreen(mouseX, mouseY, partialTicks)
        }
        modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks)
    }

    private fun drawCopyright(mouseX: Int, mouseY: Int) {
        drawString(fontRenderer, COPYRIGHT, copyrightWidth, height - 10, -1)
        if (mouseX > copyrightWidth && mouseX < copyrightWidth + COPYRIGHT_WIDTH && mouseY > height - 10 && mouseY < height && Mouse.isInsideWindow()) {
            drawRect(copyrightWidth, height - 1, copyrightWidth + COPYRIGHT_WIDTH, height, -1)
        }
    }

    private fun drawBrandings() {
        FMLCommonHandler.instance().getBrandings(true).reversed().forEachIndexed { index, branding ->
            if (branding?.isNotEmpty() == true) {
                drawString(fontRenderer, branding, 2, height - (10 + index * (fontRenderer.FONT_HEIGHT + 1)), 16777215)
            }
        }
    }

    private fun drawSplash() {
        GlStateManager.pushMatrix()
        GlStateManager.translate((width / 2 + 90).toFloat(), 70.0f, 0.0f)
        GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f)
        var f = 1.8f - MathHelper.abs(MathHelper.sin((Minecraft.getSystemTime() % 1000L).toFloat() / 1000.0f * (Math.PI.toFloat() * 2f)) * 0.1f)
        f = f * 100.0f / (fontRenderer.getStringWidth(splashText) + 32).toFloat()
        GlStateManager.scale(f, f, f)
        drawCenteredString(fontRenderer, splashText, 0, -8, CustomMenu.splashColor.rgba)
        GlStateManager.popMatrix()
    }

    private fun drawTitle() {
        mc.textureManager.bindTexture(TITLE)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        val titleWidth = width / 2 - 137
        if (minceraftRoll < 1.0E-4) {
            this.drawTexturedModalRect(titleWidth + 0, 30, 0, 0, 99, 44)
            this.drawTexturedModalRect(titleWidth + 99, 30, 129, 0, 27, 44)
            this.drawTexturedModalRect(titleWidth + 99 + 26, 30, 126, 0, 3, 44)
            this.drawTexturedModalRect(titleWidth + 99 + 26 + 3, 30, 99, 0, 26, 44)
            this.drawTexturedModalRect(titleWidth + 155, 30, 0, 45, 155, 44)
        } else {
            this.drawTexturedModalRect(titleWidth + 0, 30, 0, 0, 155, 44)
            this.drawTexturedModalRect(titleWidth + 155, 30, 0, 45, 155, 44)
        }
        if (!CustomMenu.noEdition.value) {
            mc.textureManager.bindTexture(EDITION)
            drawModalRectWithCustomSizedTexture(titleWidth + 88, 67, 0.0f, 0.0f, 98, 14, 128.0f, 16.0f)
        }
    }

    private fun drawPanorama() {
        val tessellator = Tessellator.getInstance()
        val bufferbuilder = tessellator.buffer
        GlStateManager.matrixMode(5889)
        GlStateManager.pushMatrix()
        GlStateManager.loadIdentity()
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f)
        GlStateManager.matrixMode(5888)
        GlStateManager.pushMatrix()
        GlStateManager.loadIdentity()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f)
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f)
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.disableCull()
        GlStateManager.depthMask(false)
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
        val i = 8
        for (j in 0..63) {
            GlStateManager.pushMatrix()
            val f = ((j % 8).toFloat() / 8.0f - 0.5f) / 64.0f
            val f1 = ((j / 8).toFloat() / 8.0f - 0.5f) / 64.0f
            val f2 = 0.0f
            GlStateManager.translate(f, f1, 0.0f)
            GlStateManager.rotate(MathHelper.sin(panoramaTimer / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f)
            GlStateManager.rotate(-panoramaTimer * 0.1f, 0.0f, 1.0f, 0.0f)
            for (k in 0..5) {
                GlStateManager.pushMatrix()
                if (k == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f)
                }
                if (k == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
                }
                if (k == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f)
                }
                if (k == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f)
                }
                if (k == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                }
                mc.textureManager.bindTexture(PANORAMAS[k])
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
                val l = 255 / (j + 1)
                val f3 = 0.0f
                bufferbuilder.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, l).endVertex()
                bufferbuilder.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, l).endVertex()
                bufferbuilder.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, l).endVertex()
                bufferbuilder.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, l).endVertex()
                tessellator.draw()
                GlStateManager.popMatrix()
            }
            GlStateManager.popMatrix()
            GlStateManager.colorMask(true, true, true, false)
        }
        bufferbuilder.setTranslation(0.0, 0.0, 0.0)
        GlStateManager.colorMask(true, true, true, true)
        GlStateManager.matrixMode(5889)
        GlStateManager.popMatrix()
        GlStateManager.matrixMode(5888)
        GlStateManager.popMatrix()
        GlStateManager.depthMask(true)
        GlStateManager.enableCull()
        GlStateManager.enableDepth()
    }

    private fun rotateAndBlurSkybox() {
        mc.textureManager.bindTexture(backgroundTexture)
        GlStateManager.glTexParameteri(3553, 10241, 9729)
        GlStateManager.glTexParameteri(3553, 10240, 9729)
        GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
        GlStateManager.colorMask(true, true, true, false)
        val tessellator = Tessellator.getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        GlStateManager.disableAlpha()
        val i = 3
        for (j in 0..2) {
            val f = 1.0f / (j + 1).toFloat()
            val k = width
            val l = height
            val f1 = (j - 1).toFloat() / 256.0f
            bufferbuilder.pos(k.toDouble(), l.toDouble(), zLevel.toDouble()).tex((0.0f + f1).toDouble(), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
            bufferbuilder.pos(k.toDouble(), 0.0, zLevel.toDouble()).tex((1.0f + f1).toDouble(), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
            bufferbuilder.pos(0.0, 0.0, zLevel.toDouble()).tex((1.0f + f1).toDouble(), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
            bufferbuilder.pos(0.0, l.toDouble(), zLevel.toDouble()).tex((0.0f + f1).toDouble(), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
        }
        tessellator.draw()
        GlStateManager.enableAlpha()
        GlStateManager.colorMask(true, true, true, true)
    }

    private fun renderSkybox(partialTicks: Float) {
        panoramaTimer += partialTicks
        GlStateManager.disableAlpha()
        mc.getFramebuffer().unbindFramebuffer()
        GlStateManager.viewport(0, 0, 256, 256)
        drawPanorama()
        repeat(7) { rotateAndBlurSkybox() }
        mc.getFramebuffer().bindFramebuffer(true)
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight)
        val f = 120.0f / (if (width > height) width else height).toFloat()
        val f1 = height.toFloat() * f / 256.0f
        val f2 = width.toFloat() * f / 256.0f
        val tessellator = Tessellator.getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        bufferbuilder.pos(0.0, height.toDouble(), zLevel.toDouble()).tex((0.5f - f1).toDouble(), (0.5f + f2).toDouble()).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        bufferbuilder.pos(width.toDouble(), height.toDouble(), zLevel.toDouble()).tex((0.5f - f1).toDouble(), (0.5f - f2).toDouble()).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        bufferbuilder.pos(width.toDouble(), 0.0, zLevel.toDouble()).tex((0.5f + f1).toDouble(), (0.5f - f2).toDouble()).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        bufferbuilder.pos(0.0, 0.0, zLevel.toDouble()).tex((0.5f + f1).toDouble(), (0.5f + f2).toDouble()).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        tessellator.draw()
        GlStateManager.enableAlpha()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        if (!CustomMenu.noRealms.value && mc.gameSettings.realmsNotifications && realmsScreen != null) {
            //realmsScreen!!.mouseClicked(mouseX, mouseY, mouseButton)
        }
        if (mouseX > copyrightWidth && mouseX < copyrightWidth + COPYRIGHT_WIDTH && mouseY > height - 10 && mouseY < height) {
            mc.displayGuiScreen(GuiWinGame(false) {})
        }
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            1 -> mc.displayGuiScreen(GuiWorldSelection(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            4 -> mc.shutdown()
            5 -> mc.displayGuiScreen(GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()))
            6 -> mc.displayGuiScreen(GuiModList(this))
            14 -> if (realmsButton.visible) RealmsBridge().switchToRealms(this)
            711 -> mc.displayGuiScreen(ClickGuiScreen)
        }
    }

    override fun updateScreen() {
        if (!CustomMenu.noRealms.value && mc.gameSettings.realmsNotifications && realmsScreen != null) {
            realmsScreen!!.updateScreen()
        }
    }

    override fun onGuiClosed() {
        if (!CustomMenu.noRealms.value) {
            realmsScreen?.onGuiClosed()
        }
    }

    override fun doesGuiPauseGame() = false

    companion object {
        private val TITLE = ResourceLocation("textures/gui/title/minecraft.png")
        private val EDITION = ResourceLocation("textures/gui/title/edition.png")
        private val PANORAMAS = arrayOf(
            ResourceLocation("textures/gui/title/background/panorama_0.png"),
            ResourceLocation("textures/gui/title/background/panorama_1.png"),
            ResourceLocation("textures/gui/title/background/panorama_2.png"),
            ResourceLocation("textures/gui/title/background/panorama_3.png"),
            ResourceLocation("textures/gui/title/background/panorama_4.png"),
            ResourceLocation("textures/gui/title/background/panorama_5.png")
        )
        private const val COPYRIGHT = "Copyright Mojang AB. Do not distribute!"
        private val COPYRIGHT_WIDTH = FontRenderer.getStringWidth(COPYRIGHT)
    }
}
