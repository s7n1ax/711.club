package me.bush.cornerstore.api.shader

import me.bush.cornerstore.api.common.Nameable
import me.bush.cornerstore.util.lang.unsafeLazy
import me.bush.cornerstore.util.render.ShaderUtil
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.GL20

/**
 * Add uniforms in `init { }` using [uniform]
 *
 * Shouldn't be fuckin with gl in here, access everything
 * through [ShaderUtil]
 *
 * Set [name] to what the shader should be displayed as, and the path will be resolved by
 * turning the name to lowercase, appending ".vert", and prefixing it with the path to
 * our shaders folder.
 *
 * @author bush
 * @since 3/9/2022
 */
abstract class Shader : Nameable {
    private val program by unsafeLazy {
        ShaderUtil.createProgram("assets/cornerstore/shaders/${name.lowercase()}.frag")
    }
    private val uniforms by unsafeLazy {

        ShaderUtil.mapUniforms(program)
    }

    /**
     * Use the `glUniformX()` functions in [ShaderUtil] or [OpenGlHelper] to set uniform values.
     * This function is called in [startUse], ideally every frame.
     *
     * Avoid using [GL20] or [ARBShaderObjects], as they may not work for some people.
     * The former classes check which OpenGL implementation is in use to determine compatibility.
     */
    abstract fun updateUniforms()

    protected fun uniform(name: String) = uniforms[name]!!

    inline fun use(x: Int, y: Int, w: Int, H: Int, block: () -> Unit) {
        startUse()
        block()
        stopUse()
    }

    fun startUse() {
        OpenGlHelper.glUseProgram(program)
        updateUniforms()
    }

    fun stopUse() {
        OpenGlHelper.glUseProgram(0)
    }

    // Called by the gc before this shader is deallocated,
    // no need to manually delete anything
    protected fun finalize() {
        OpenGlHelper.glDeleteProgram(program)
    }
}
