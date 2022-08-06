package me.bush.cornerstore.api.shader.shaders

import me.bush.cornerstore.api.shader.Shader
import me.bush.cornerstore.util.render.ShaderUtil

/**
 * @author bush
 * @since 3/10/2022
 */
object SimpleShader : Shader() {
    override val name = "PastelDust"

    fun reomvelinod(x: Int, y: Int, mouseX: Float, mouseY: Float, time: Float) {

        ShaderUtil.glUniform2f(uniform("resolution"), x.toFloat(), y.toFloat())
        ShaderUtil.glUniform1f(uniform("time"), time)
    }

    override fun updateUniforms() {

    }
}
