/**
 * File name : Shader.java
 * Created by: Jaco Wiese (CP325745)
 * Created on: 08 Sep 2017 at 12:56:29 PM
 */

package org.jacowiese.shader;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import org.lwjgl.system.MemoryStack;

/**
 * 
 * @author Jaco Wiese (CP325745)
 */
public class Shader {

	private final int programId;
	private final Map<String, Integer> uniforms;

	private int vertexShaderId;
	private int fragmentShaderId;

	public Shader() throws Exception {
		programId = glCreateProgram();
		if (programId == 0) {
			throw new Exception("Could not create shader program.");
		}

		uniforms = new HashMap();
	}

	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}

	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking shader code: " + glGetProgramInfoLog(programId, 1024));
		}

		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			throw new Exception("Warning validating shader code: " + glGetProgramInfoLog(programId, 1024));
		}
	}

	public void bind() {
		glUseProgram(programId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanup() {
		unbind();

		if (programId != 0) {

			if (vertexShaderId != 0) {
				glDetachShader(programId, vertexShaderId);
			}

			if (fragmentShaderId != 0) {
				glDetachShader(programId, fragmentShaderId);
			}

			glDeleteProgram(programId);
		}
	}

	public void createUniform(String uniformName) throws Exception {
		int loc = glGetUniformLocation(programId, uniformName);
		if (loc < 0) {
			throw new Exception("Could not find uniform: " + uniformName);
		}

		uniforms.put(uniformName, loc);
	}

	public void setUniform(String uniformName, Matrix4f matrix) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			matrix.get(fb);
			glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
		}
	}

	public void setUniform(String uniformName, int textureSlot) {
		glUniform1i(uniforms.get(uniformName), textureSlot);
	}

	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Code: " + shaderId);
		}

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling shader code: " + GL20.glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(programId, shaderId);

		return shaderId;
	}

}
