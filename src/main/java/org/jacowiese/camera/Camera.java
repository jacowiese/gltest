/**
 * File name : Camera.java
 * Created by: Jaco Wiese (CP325745)
 * Created on: 13 Sep 2017 at 1:16:10 PM
 */

package org.jacowiese.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * 
 * @author Jaco Wiese (CP325745)
 */
public class Camera {

	private Vector3f eye;
	private Vector3f at;
	private Vector3f up;

	public Camera() {
		this.eye = new Vector3f(0f ,3f, -5f);
		this.at = new Vector3f(0f, 0f, 0f);
		this.up = new Vector3f(0f, 1f, 0f);
	}
	
	public Matrix4f getViewMatrix() {
		return new Matrix4f().lookAt(eye, at, up);
	}
	
	public Vector3f getEye() {
		return eye;
	}

	public void setEye(Vector3f eye) {
		this.eye = eye;
	}

	public Vector3f getAt() {
		return at;
	}

	public void setAt(Vector3f at) {
		this.at = at;
	}

	public Vector3f getUp() {
		return up;
	}

	public void setUp(Vector3f up) {
		this.up = up;
	}		
	
}
