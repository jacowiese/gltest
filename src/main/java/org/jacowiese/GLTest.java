/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacowiese;

import org.jacowiese.camera.Camera;
import org.jacowiese.mesh.GameObject;
import org.jacowiese.mesh.Mesh;
import org.jacowiese.shader.Shader;
import org.jacowiese.texture.Texture2D;
import org.jacowiese.util.FileUtils;
import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

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
        1f, 1f, 0.0f};

    private float[] colors = new float[]{
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f};

    private float[] texcoords = new float[]{
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f};

    private int[] indices = new int[]{0, 1, 2, 0, 2, 3};

    private Mesh mesh;
    private GameObject gameObj;
    private Camera camera;
    private Texture2D texture;

    private int texId;

    private void init() throws Exception {
        System.setProperty("org.lwjgl.util.Debug", "true");

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "GL Test", 0, 0);
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
        myShader.createVertexShader(FileUtils.loadResource("shaders/vertextex.vs"));
        myShader.createFragmentShader(FileUtils.loadResource("shaders/fragmenttex.fs"));
        myShader.link();
        myShader.createUniform("projectionMatrix");
        myShader.createUniform("viewMatrix");
        myShader.createUniform("worldMatrix");
        myShader.createUniform("texture_sampler");

        mesh = new Mesh();
        mesh.createMeshTextured(vertices, indices, texcoords);

        gameObj = new GameObject();
        gameObj.setMesh(mesh);

        camera = new Camera();

        texture = FileUtils.loadPNG("textures/monkey.png");

//        ByteBuffer meh = ByteBuffer.allocateDirect(4 * 512 * 512);
//        for (int i =0 ; i < 4 * 512 * 512; i++) {
//            meh.put((byte)128);            
//        }
//        meh.flip();
//        texture.setTextureBuffer(meh);
        texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                texture.getTextureBuffer());
        // Generate mipmaps
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        // Enable depth test (z-buffer)
        glEnable(GL_DEPTH_TEST);
    }

    private void loop() {

        glClearColor(0.3f, 0.6f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float aspectRatio = (float) 800f / 600f;
            Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(60.0f), aspectRatio, 0.01f, 1000.0f);

            Matrix4f viewMatrix = camera.getViewMatrix();

            gameObj.getTransform().getRotation().rotateLocalY((float) Math.toRadians(1f));

            myShader.bind();
            myShader.setUniform("projectionMatrix", projection);
            myShader.setUniform("viewMatrix", viewMatrix);
            myShader.setUniform("worldMatrix", gameObj.getTransform().getWorldMatrix());
            myShader.setUniform("texture_sampler", 0);
            mesh.drawTextured(texId);
            myShader.unbind();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

    }

    private void destroy() {
        mesh.cleanup();

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
