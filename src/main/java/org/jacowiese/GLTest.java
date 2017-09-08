/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacowiese;

import org.jacowiese.shader.Shader;
import org.jacowiese.util.FileUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 *
 * @author Snowy
 */
public class GLTest {

    // Window handle
    private long window;

    private Shader myShader;
    
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
		
	myShader = new Shader();
	myShader.createVertexShader(FileUtils.loadResource("shaders/vertex.vs"));
	myShader.createFragmentShader(FileUtils.loadResource("shaders/fragment.fs"));
	myShader.link();
    }

    private void loop() {

        GL.createCapabilities();

        GL11.glClearColor(0.3f, 0.6f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	    myShader.bind();
	    
	    myShader.unbind();
	    
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

    }

    private void destroy() {
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
