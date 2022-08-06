package me.bush.cornerstore.api.load

import me.bush.cornerstore.CornerStore
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins

/**
 * @author perry, bush.
 * @since 11/10/2021.
 */
@Name(value = CornerStore.NAME + " Mixin Loader")
@MCVersion(value = "1.12.2")
class MixinLoader : IFMLLoadingPlugin {

    init {
        MixinBootstrap.init()
        Mixins.addConfiguration("mixins.cornerstore.json")
        MixinEnvironment.getDefaultEnvironment().obfuscationContext = "searge"
    }

    override fun getASMTransformerClass() = emptyArray<String>()

    override fun getModContainerClass(): String? = null

    override fun getSetupClass(): String? = null

    override fun injectData(data: Map<String, Any>) = Unit

    override fun getAccessTransformerClass(): String? = null
}
