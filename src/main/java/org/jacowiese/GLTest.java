/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacowiese;

import java.nio.FloatBuffer;
import org.jacowiese.shader.Shader;
import org.jacowiese.util.FileUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL.createCapabilities;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
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
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    };

    private int vaoId;
    private int vboId;

    FloatBuffer verticesBuffer;

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

        myShader = new Shader();
        myShader.createVertexShader(FileUtils.loadResource("shaders/vertex.vs"));
        myShader.createFragmentShader(FileUtils.loadResource("shaders/fragment.fs"));
        myShader.link();

        verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        memFree(verticesBuffer);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    private void loop() {

        glClearColor(0.3f, 0.6f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            myShader.bind();

            // Bind to the VAO
            glBindVertexArray(vaoId);
            glEnableVertexAttribArray(0);

            // Draw the vertices
            glDrawArrays(GL_TRIANGLES, 0, 3);
            // Restore state
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);

            myShader.unbind();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

    }

    private void destroy() {
        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // Unbind the VAO
        glBindVertexArray(0);
        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer);
        }

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
