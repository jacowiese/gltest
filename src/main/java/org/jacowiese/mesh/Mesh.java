/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacowiese.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glIndexPointer;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
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
public class Mesh {

    private int vaoId;
    private int vboId;
    private int iboId;
    private int colorVboId;
    private int textureVboId;

    private boolean hasColor;
    private boolean hasTexture;

    private int vertexCount;
    private int indexCount;

    public Mesh() {
    }

    public void createMeshColored(float[] vertices, int[] indices, float[] colors) {
        hasColor = true;
        hasTexture = false;

        vertexCount = vertices.length / 3;
        indexCount = indices.length;

        // Create the VAO
        vaoId = glGenBuffers();
        glBindVertexArray(vaoId);

        // Create the vertex buffer
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        memFree(verticesBuffer);

        // Create the index buffer
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        glIndexPointer(4, indicesBuffer);
        // glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        memFree(indicesBuffer);

        // Create the color buffer
        FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(colors.length);
        colorBuffer.put(colors).flip();

        colorVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        memFree(colorBuffer);

        // Unbind the VAO
        glBindVertexArray(0);
    }

    public void createMeshTextured(float[] vertices, int[] indices, float[] texcoords) {
        hasColor = false;
        hasTexture = true;

        vertexCount = vertices.length / 3;
        indexCount = indices.length;

        // Create the VAO
        vaoId = glGenBuffers();
        glBindVertexArray(vaoId);

        // Create the vertex buffer
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        memFree(verticesBuffer);

        // Create the index buffer
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        glIndexPointer(4, indicesBuffer);
        // glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        memFree(indicesBuffer);

        // Create the texture buffer
        FloatBuffer texcoordBuffer = MemoryUtil.memAllocFloat(texcoords.length);
        texcoordBuffer.put(texcoords).flip();

        textureVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
        glBufferData(GL_ARRAY_BUFFER, texcoordBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        memFree(texcoordBuffer);

        // Unbind the VAO
        glBindVertexArray(0);
    }

    public void draw() {
        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0); // Vertex data
        if (hasColor) {
            glEnableVertexAttribArray(1); // Color data
        }
        if (hasTexture) {
            glEnableVertexAttribArray(1);
        }

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        if (hasColor) {
            glDisableVertexAttribArray(1);
        }
        if (hasTexture) {
            glDisableVertexAttribArray(1);
        }
        glBindVertexArray(0);
    }

    public void drawTextured(int texture0) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture0);

        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0); // Vertex data
        glEnableVertexAttribArray(1); // Texcoords

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);

    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the vbos
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(iboId);

        // Delete the vao
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
        if (hasColor) {
            glDisableVertexAttribArray(1);
            glDeleteBuffers(colorVboId);
        }
        if (hasTexture) {
            glDisableVertexAttribArray(1);
            glDeleteBuffers(textureVboId);
        }

    }

}
