package me.bush.cornerstore.impl.module.modules.movement

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.MoveEvent
import me.bush.cornerstore.api.setting.settings.FloatSetting
import me.bush.cornerstore.api.setting.settings.IntSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.math.pythag
import me.bush.cornerstore.util.minecraft.*
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.*
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.*
import net.minecraft.util.math.RayTraceResult.Type
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import org.lwjgl.opengl.GL11
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.*


/**
 * @author bush
 * @since 5/12/2022
 */
object StrafeAssist : Module("Alters your trajectory to always land you on y=5 bedrock") {
    private val angle by FloatSetting("Search Angle", 80f, 1f, 120f).withInfo("How wide of an area to search for target blocks")
    private val maxSteps by IntSetting("Calculated Ticks", 20, 4, 80).withInfo("How many ticks into the future to calculate. This doesn't really need to be above 20")
    private val widthResolution by FloatSetting("Ray Density", 0.2f, 0.01f, 1f).withInfo("How many rays to trace")
    private val maxSpeed by FloatSetting("Max Speed", 0.5f, 0f, 3f).withInfo("The fastest left/right movement")
    private val calcDelay by IntSetting("Calc Delay", 2, 0, 10).withInfo("How many ticks to wait after jumping to start calculations")
    // Values in mc.player won't work for this
    var offGroundTicks = 0
    var lastX = 0.0
    var lastZ = 0.0

    @EventListener
    fun onMove() = listener<MoveEvent> { event ->
        if (mc.player.onGround) offGroundTicks = 0
        else {
            offGroundTicks++
            if (offGroundTicks >= calcDelay && mc.player.shouldMovementHack()) {
                doStrafeAssist(event)
                lastX = mc.player.posX
                lastZ = mc.player.posZ
            }
        }
    }

    private fun doStrafeAssist(event: MoveEvent) {
        val speed = pythag(mc.player.posX - lastX, mc.player.posZ - lastZ)
        var motionY = mc.player.motionY
        var posY = mc.player.posY
        // Each step is one predicted tick forward
        for (step in 0..maxSteps) {
            val stepDistance = speed * step
            val rayCount = (stepDistance * tan(angle / 360f * PI) / widthResolution * 2).toInt()
            val startXZ = mc.player.getDirectionComponents(stepDistance)
            val endXZ = mc.player.getDirectionComponents(stepDistance + speed)
            // Every ray in the current step
            for (rayIndex in 0..rayCount) {
                // Middle out, we want to find a block as close to center as we can
                val offsetFactor = if (rayIndex % 2 == 0) rayIndex else -rayIndex
                // We flip these and invert one to offset 90deg from our move direction
                val offsetZX = mc.player.getDirectionComponents(offsetFactor * widthResolution / 2.0)
                // Start/end positions of our current ray
                val startX = startXZ[0] + offsetZX[1] + mc.player.posX
                val startZ = startXZ[1] - offsetZX[0] + mc.player.posZ
                val endX = endXZ[0] + offsetZX[1] + mc.player.posX
                val endZ = endXZ[1] - offsetZX[0] + mc.player.posZ
                val rayStart = Vec3d(startX, posY, startZ)
                val rayEnd = Vec3d(endX, posY + motionY, endZ)
                // remove later
                things += { drawLineFromPosToPos(rayStart.x, rayStart.y, rayStart.z, rayEnd.x, rayEnd.y, rayEnd.z) }
                val result = mc.world.rayTraceBlocks(rayStart, rayEnd)
                // Are we going to land on the top side of a block
                if (result?.typeOfHit == Type.BLOCK && result.sideHit == EnumFacing.UP) {
                    pos = result.blockPos
                    // Are there blocks above our pos, and is our pos a block we can walk on
                    if (pos.up().isAirBlock && pos.up(2).isAirBlock && !pos.isReplaceable) {
                        val distance = min(pythag(offsetZX[1], offsetZX[0]), maxSpeed.toDouble())
                        // Can we make it to this pos if we are only going a set speed per tick
                        if (distance / step <= maxSpeed) {
                            val components = mc.player.getDirectionComponents(distance)
                            // I know theres a simpler way of doing this but I don't feel like doing math rn
                            val leftDistance = pythag(startX - mc.player.posX - components[1], startZ - mc.player.posZ + components[0])
                            val rightDistance = pythag(startX - mc.player.posX + components[1], startZ - mc.player.posZ - components[0])
                            if (leftDistance < rightDistance) {
                                event.x += components[1]
                                event.z -= components[0]
                            } else {
                                event.x -= components[1]
                                event.z += components[0]
                            }
                            // Don't move any more this tick, next iterations should keep moving to this block
                            return
                        }
                    }
                }
            }
            // Simulate y gravity motion
            posY += motionY
            motionY -= 0.08
            motionY *= 0.9800000190734863
        }
    }


    // DONT LOOK DONT LOOK DONT LOOK DONT LOOK


    // ILL DELETE THIS WHEN IM DONE DEBUGGING DONT LOOK AT IT

    // pasted
    private fun drawLineFromPosToPos(
        posx: Double,
        posy: Double,
        posz: Double,
        posx2: Double,
        posy2: Double,
        posz2: Double,
    ) {
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(3042)
        GL11.glLineWidth(3F)
        GL11.glDisable(3553)
        GL11.glDisable(2929)
        GL11.glDepthMask(false)
        GL11.glColor4f(1f, 1f, 1f, 1f)
        GlStateManager.disableLighting()
        GL11.glLoadIdentity()
        mc.entityRenderer.orientCamera(mc.renderPartialTicks)
        GL11.glBegin(1)
        GL11.glVertex3d(posx - mc.player.posX, posy - mc.player.posY + mc.player.eyeHeight, posz - mc.player.posZ)
        GL11.glVertex3d(posx2 - mc.player.posX, posy2 - mc.player.posY + mc.player.eyeHeight, posz2 - mc.player.posZ)
        //GL11.glVertex3d(posx2 - mc.player.posX, posy2 - mc.player.posY + mc.player.eyeHeight, posz2 - mc.player.posZ)
        GL11.glEnd()
        GL11.glEnable(3553)
        GL11.glEnable(2929)
        GL11.glDepthMask(true)
        GL11.glDisable(3042)
        GL11.glColor3d(1.0, 1.0, 1.0)
        GlStateManager.enableLighting()
    }

    // fucking cba rn
    val things = CopyOnWriteArrayList<() -> Unit>()
    var pos = BlockPos(1, 1, 1)
    @EventListener
    fun fuckoff() = listener<RenderWorldLastEvent> {
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425)
        GlStateManager.disableDepth()
        GlStateManager.glLineWidth(1.0f)
        val viewPort = GLAllocation.createDirectIntBuffer(16)
        val modelView = GLAllocation.createDirectFloatBuffer(16)
        val projectionPort = GLAllocation.createDirectFloatBuffer(16)
        GL11.glGetFloat(2982, modelView)
        GL11.glGetFloat(2983, projectionPort)
        GL11.glGetInteger(2978, viewPort)
        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())


        // here
        things.forEach { it() }

        drawLineFromPosToPos(0.0, 0.0, 0.0, mc.player.posX, mc.player.posY, mc.player.posZ)
        drawBox(pos)


        GlStateManager.glLineWidth(1.0f)
        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.enableCull()
        GlStateManager.enableCull()
        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.enableDepth()


    }

    @EventListener
    val tick = listener<PlayerTickEvent> {
        if (it.phase == TickEvent.Phase.START)
            things.clear()
    }

    private fun drawBox(pos: BlockPos) {
        val bb = AxisAlignedBB(pos.x - mc.getRenderManager().viewerPosX, pos.y - mc.getRenderManager().viewerPosY, pos.z - mc.getRenderManager().viewerPosZ, pos.x + 1 - mc.getRenderManager().viewerPosX, pos.y + 1 - mc.getRenderManager().viewerPosY, pos.z + 1 - mc.getRenderManager().viewerPosZ).grow(0.1)
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        RenderGlobal.renderFilledBox(bb, 1f, 1f, 1f, 1f)
        GL11.glDisable(2848)
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }


}
