/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacowiese.mesh;

/**
 *
 * @author Snowy
 */
public class GameObject {
    
    private Mesh mesh;
    private Transform transform;

    public GameObject() {
        transform = new Transform();
    }
    
    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }    
    
}
