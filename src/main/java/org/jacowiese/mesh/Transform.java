/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacowiese.mesh;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author Snowy
 */
public class Transform {
    
    private Vector3f position;
    
    private Vector3f scale;
    
    private Quaternionf rotation;

    public Transform() {
        position = new Vector3f(0f,0f,0f);
        scale = new Vector3f(1f,1f,1f);
        rotation = new Quaternionf().identity();
    }

    public Matrix4f getWorldMatrix() {
        Matrix4f tranMat = new Matrix4f().translate(position);
        Matrix4f scaleMat = new Matrix4f().scale(scale);
        Matrix4f rotMat = new Matrix4f().rotate(rotation);
        Matrix4f worldMatrix = new Matrix4f();
        return worldMatrix.mul(tranMat).mul(scaleMat).mul(rotMat);
    }
    
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }
            
}
