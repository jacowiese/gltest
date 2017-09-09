/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacowiese;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.jacowiese.shader.Shader;
import org.jacowiese.util.FileUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glDeleteVertexArrays;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL.createCapabilities;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glIndexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 *
 * @author Snowy
 */
public class GLTest {

    // Window handle
    private long window;

    private Shader myShader;

    private float[] vertices = new float[]{
        -1f, 1f, 0.0f,
        -1f, -1f, 0.0f,
        1f, -1f, 0.0f,
        1f, 1f, 0.0f
    };

    private float[] colors = new float[]{
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f
    };

    private int[] indices = new int[]{
        0, 1, 2,
        0, 2, 3
    };

    private int vaoId;
    private int vboId;
    private int iboId;
    private int colorvboId;

    FloatBuffer verticesBuffer;
    IntBuffer indicesBuffer;
    FloatBuffer colorBuffer;

    private void init() throws Exception {
        System.setProperty("org.lwjgl.util.Debug", "true");

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(300, 300, "GL Test", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create glfw Window.");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        createCapabilities();

        // Create a shader
        myShader = new Shader();
        myShader.createVertexShader(FileUtils.loadResource("shaders/vertex.vs"));
        myShader.createFragmentShader(FileUtils.loadResource("shaders/fragment.fs"));
        myShader.link();

        // Set up the vao
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Set up the vertex buffer
        verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        memFree(verticesBuffer);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer);
        }

        // Set up in the index buffer
        indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        memFree(indicesBuffer);

        glIndexPointer(4, indicesBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Set up the color buffer
        colorBuffer = MemoryUtil.memAllocFloat(colors.length);
        colorBuffer.put(colors).flip();

        colorvboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorvboId);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        memFree(colorBuffer);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // Unbind the colorVBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (colorBuffer != null) {
            MemoryUtil.memFree(colorBuffer);
        }

        // Unbind the VAO
        glBindVertexArray(0);

    }

    private void loop() {

        glClearColor(0.3f, 0.6f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            myShader.bind();

            // Bind to the VAO
            glBindVertexArray(vaoId);
            glEnableVertexAttribArray(0); // Vertex data
            glEnableVertexAttribArray(1); // Color data

            // Draw the vertices
            //glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 3);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            // Restore state
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);

            myShader.unbind();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

    }

    private void destroy() {
        glDisableVertexAttribArray(0);

        // Delete the vbos
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(iboId);
        glDeleteBuffers(colorvboId);

        // Delete the vao
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);

        myShader.cleanup();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate glfw and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void run() throws Exception {
        System.out.println(String.format("LWJGL %s.", Version.getVersion()));

        init();
        loop();
        destroy();

    }

    public static void main(String[] args) throws Exception {
        new GLTest().run();
    }

}
