package me.bush.cornerstore.mixin

import me.bush.cornerstore.util.minecraft.SplashUtil
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiScreen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author bush
 * @since 3/11/2022
 */
@Mixin(GuiMainMenu::class)
abstract class MixinGuiMainMenu : GuiScreen() {

    //    @Inject(method = "drawScreen", at = @At("HEAD"))
    //    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
    //        //if (true) {
    //        //GlStateManager.disableCull();
    //        // TODO: 3/12/2022 lighting has to do with the gradient i think
    //        //GlStateManager.enableLighting();
    //        SimpleShader.INSTANCE.startUse();
    //        // DONT LOOK AT THE CODE
    //        // TODO: 3/12/2022 fb thing
    //        // TODO: 3/12/2022 shader setting idk
    //        SimpleShader.INSTANCE.reomvelinod(this.width * 2, this.height * 2, 0, 0, (SessionManager.INSTANCE.getElapsedMs()) / 1000f);
    //        //Main.shaders.currentshader.useShader( width * 2, height * 2, mouseX * 2, mouseY * 2, ( System.currentTimeMillis( ) - Main.shaders.time ) / 1000f );
    //        // TODO: 3/12/2022 how do this work
    //        GL11.glBegin(GL11.GL_QUADS);
    //        GL11.glVertex2f(-1f, -1f);
    //        GL11.glVertex2f(-1f, 1f);
    //        GL11.glVertex2f(1f, 1f);
    //        GL11.glVertex2f(1f, -1f);
    //        GL11.glEnd();
    //        SimpleShader.INSTANCE.stopUse();
    ////            GL20.glUseProgram( 0 );
    //        //}
    //        //Logger.INSTANCE.info("after stupuse");
    //        //GlStateManager.enableCull();
    //        //Logger.INSTANCE.info("after cull");
    //        //GlStateManager.enableLighting();
    //        //GlStateManager.disableLighting();
    //        // TODO: 3/12/2022 fix stupid error thing
    //
    //    }
    //
    //    @Inject(method = "renderSkybox", at = @At("HEAD"), cancellable = true)
    //    public void renderSkybox(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
    //        // DONT LOOK AT IT
    //        if (true) info.cancel();
    //    }

    @Shadow
    private lateinit var splashText: String

    @Inject(method = ["initGui"], at = [At("RETURN")])
    fun initGui(callback: CallbackInfo) {
        splashText = SplashUtil.getRandomSplash()
    }
}
