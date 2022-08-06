package me.bush.cornerstore.util.render

import me.bush.cornerstore.api.common.Logger
import me.bush.cornerstore.util.system.getResourceAsString
import net.minecraft.client.renderer.OpenGlHelper.*
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.GL20

/**
 * @author bush
 * @since 3/10/2022
 */
object ShaderUtil {

    // Could update to support multiple passes/shaders?
    fun createProgram(frag: String, vert: String = "assets/cornerstore/shaders/passthrough.vert"): Int {
        // Create program object
        val program = glCreateProgram().also {
            if (it == 0) throw ShaderCreationException("Unable to allocate program object. Using $frag and $vert")
        }
        // Attach shaders to program
        val fragmentShader = createShader(frag, GL_FRAGMENT_SHADER)
        val vertexShader = createShader(vert, GL_VERTEX_SHADER)
        glAttachShader(program, fragmentShader)
        glAttachShader(program, vertexShader)
        // I can't say I know what this is for yet
        glLinkProgram(program)
        if (linkFailed(program)) {
            Logger.error(programLog(program))
            glDeleteProgram(program)
            throw ShaderCreationException("Unable to link shader program. Using $frag and $vert")
        }
        // Make sure shit works
        glValidateProgram(program)
        // If it dont
        if (validateFailed(program)) {
            Logger.error(programLog(program))
            glDeleteProgram(program)
            throw ShaderCreationException("Unable to validate shader program. Using $frag and $vert")
        }
        // Marks shaders for deletion, and they get automatically
        // deleted when the program is deleted
        glDeleteShader(fragmentShader)
        glDeleteShader(vertexShader)
        return program
    }

    private fun createShader(path: String, type: Int): Int {
        // Throw if we can't find the shader
        val source = this::class.getResourceAsString(path)
        // Allocate shader object
        val shader = glCreateShader(type).also {
            if (it == 0) throw ShaderCreationException("Unable to allocate shader object for $path")
        }
        // Compile shader
        glShaderSource(shader, source)
        glCompileShader(shader)
        // If shader could not compile, log info, deallocate shader, and throw.
        if (compileFailed(shader)) {
            Logger.error(shaderLog(shader))
            glDeleteShader(shader)
            throw ShaderCreationException("Unable to compile shader $path")
        }
        return shader
    }

    fun mapUniforms(program: Int) = getAllUniforms(program).associateWith { setupUniform(program, it) }

    private fun getAllUniforms(program: Int) = mutableListOf<String>().apply {
        glUseProgram(program)
        repeat(glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS)) {
            add(glGetActiveUniform(program, it, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH))
        }
        glUseProgram(0)
    }

    private fun setupUniform(program: Int, name: String): Int {
        return glGetUniformLocation(program, name).also {
            if (it == -1) throw ShaderCreationException("There was an error while trying to locate uniform $name")
        }
    }

    // ARB/GL20 funcs

    fun glUniform1f(uniform: Int, value: Float) {
        if (arbShaders) ARBShaderObjects.glUniform1fARB(uniform, value)
        else GL20.glUniform1f(uniform, value)
    }

    fun glUniform2f(uniform: Int, value1: Float, value2: Float) {
        if (arbShaders) ARBShaderObjects.glUniform2fARB(uniform, value1, value2)
        else GL20.glUniform2f(uniform, value1, value2)
    }

    private fun glGetActiveUniform(program: Int, index: Int, maxLength: Int): String {
        return if (arbShaders) ARBShaderObjects.glGetActiveUniformARB(program, index, maxLength)
        else GL20.glGetActiveUniform(program, index, maxLength)
    }

    private fun glValidateProgram(program: Int) {
        if (arbShaders) ARBShaderObjects.glValidateProgramARB(program)
        else GL20.glValidateProgram(program)
    }

    private fun glShaderSource(shader: Int, source: CharSequence) {
        if (arbShaders) ARBShaderObjects.glShaderSourceARB(shader, source)
        else GL20.glShaderSource(shader, source)
    }

    // Convenience funcs

    private fun compileFailed(shader: Int) = glGetShaderi(shader, GL_COMPILE_STATUS) == 0

    private fun linkFailed(program: Int) = glGetProgrami(program, GL_LINK_STATUS) == 0

    private fun validateFailed(program: Int) = glGetProgrami(program, GL20.GL_VALIDATE_STATUS) == 0

    private fun shaderLog(shader: Int) = glGetShaderInfoLog(shader, glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH))

    private fun programLog(program: Int) = glGetProgramInfoLog(program, glGetProgrami(program, GL20.GL_INFO_LOG_LENGTH))

    private class ShaderCreationException(message: String) : Exception(message)
}
