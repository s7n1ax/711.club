package me.bush.cornerstore.impl.module.modules.render

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.setting.*
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.mixin.MixinItemRenderer
import me.bush.cornerstore.mixin.MixinRenderItem
import me.bush.cornerstore.util.render.Render3DUtil
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHandSide
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

/**
 * @author bush
 * @since 3/9/2022
 *
 * @see MixinItemRenderer
 * @see MixinRenderItem
 */
object ViewModel : Module("Modify your first person item viewmodel") {
    val noSway by BooleanSetting("No Sway", false).withInfo("Doesn't change item position when turning")
    private val noEquip by BooleanSetting("No Equip Bob", false).withInfo("Doesn't move items down when switching items")
    val alpha by IntSetting("Opacity", 255, 0, 255).withInfo("Changes item transparency")

    private val stretch = ParentSetting("Stretch")
    private val stretchX by FloatSetting("Stretch X", 1f, 0f, 3f).withParent(stretch)
    private val stretchY by FloatSetting("Stretch Y", 1f, 0f, 3f).withParent(stretch)
    private val stretchZ by FloatSetting("Stretch Z", 1f, 0f, 3f).withParent(stretch)

    private val scale = ParentSetting("Scale")
    private val scaleX by FloatSetting("Scale X", 1f, 0f, 3f).withParent(scale)
    private val scaleY by FloatSetting("Scale Y", 1f, 0f, 3f).withParent(scale)
    private val scaleZ by FloatSetting("Scale Z", 1f, 0f, 3f).withParent(scale)

    private val angle = ParentSetting("Angle")
    private val angleX by FloatSetting("Angle X", 0f, -180f, 180f).withParent(angle)
    private val angleY by FloatSetting("Angle Y", 0f, -180f, 180f).withParent(angle)
    private val angleZ by FloatSetting("Angle Z", 0f, -180f, 180f).withParent(angle)

    private val position = ParentSetting("Position")
    private val positionX by FloatSetting("Position X", 0f, -3f, 3f).withParent(position)
    private val positionY by FloatSetting("Position Y", 0f, -3f, 3f).withParent(position)
    private val positionZ by FloatSetting("Position Z", 0f, -3f, 3f).withParent(position)

    private val eating = ParentSetting("Eating")
    private val noEat by BooleanSetting("No Eat", false).withParent(eating)
    private val bobOnly by BooleanSetting("Bob Only", false).withParent(eating).withVis { !noEat }
    private val eatingX by FloatSetting("Eating X", 0f, -2f, 2f).withParent(eating).withVis { !noEat && !bobOnly }
    private val eatingY by FloatSetting("Eating Y", 0f, -2f, 2f).withParent(eating).withVis { !noEat && !bobOnly }

    fun renderItemInFirstPerson() {
        // CC viewmodel stretch
        scale(stretchX, stretchY, stretchZ)
    }

    fun transformSideFirstPerson(hand: EnumHandSide, equipProgress: Float) {
        // Simply translating back is 100x easier than making a @ModifyArgs mixin
        if (noEquip) translate(0f, equipProgress * 0.6f, 0f)
        // Item scale
        scale(scaleX, scaleY, scaleZ)
        // Item position and angle
        if (hand == EnumHandSide.LEFT) {
            translate(-positionX, positionY, -positionZ)
            Render3DUtil.rotateEuler(-angleX, -angleY, angleZ)
        } else {
            translate(positionX, positionY, -positionZ)
            Render3DUtil.rotateEuler(-angleX, angleY, -angleZ)
        }
    }

    // Pasting all the methods together here sucks but theres not a
    // better way to do it without also changing alpha of items in the gui.
    fun renderItem(stack: ItemStack, entity: EntityLivingBase?, transform: TransformType, left: Boolean) {
        if (stack.isEmpty || entity == null) return
        val renderer = mc.itemRenderer.itemRenderer
        var model = renderer.getItemModelWithOverrides(stack, entity.world, entity)

        // Enter renderItemModel(ItemStack, IBakedModel, TransformType, boolean)
        renderer.textureManager.bindTexture(LOCATION_BLOCKS_TEXTURE)
        renderer.textureManager.getTexture(LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false)
        color(1f, 1f, 1f, 1f)
        enableRescaleNormal()
        alphaFunc(516, 0.1f)
        enableBlend()
        tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO)
        pushMatrix()
        model = ForgeHooksClient.handleCameraTransforms(model, transform, left)

        // Enter renderItem(ItemStack, IBakedModel)
        translate(-0.5f, -0.5f, -0.5f)
        if (model.isBuiltInRenderer) {
            color(1f, 1f, 1f, alpha / 255f)
            enableRescaleNormal()
            stack.item.tileEntityItemStackRenderer.renderByItem(stack)
        } else {
            val color = 0xFFFFFF or ((alpha and 0xFF) shl 24)
            renderer.renderModel(model, color, stack)
            if (stack.hasEffect()) renderer.renderEffect(model)
        }
        // Exit renderItem(ItemStack, IBakedModel)

        cullFace(CullFace.BACK)
        popMatrix()
        disableRescaleNormal()
        disableBlend()
        renderer.textureManager.bindTexture(LOCATION_BLOCKS_TEXTURE)
        renderer.textureManager.getTexture(LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap()
        // Exit renderItemModel(ItemStack, IBakedModel, TransformType, boolean)
    }

    // Avoids having to use less compatible mixin strategies
    fun transformEatFirstPerson(partialTicks: Float, hand: EnumHandSide, stack: ItemStack) {
        if (noEat) return
        val time = mc.player.itemInUseCount - partialTicks + 1f
        val progress = time / stack.maxItemUseDuration
        if (progress < 0.8f) translate(0f, abs(cos(time / 4f * PI.toFloat()) * 0.1f), 0f)
        if (bobOnly) return
        val factor = 1.0F - progress.pow(27)
        val i = if (hand == EnumHandSide.RIGHT) 1f else -1f
        translate(factor * 0.6f * i * -(eatingX - 1), factor * -0.5f * -(eatingY - 1), 0f)
        Render3DUtil.rotateEuler(factor * 10f, factor * 90f * i, factor * 30f * i)
    }
}
