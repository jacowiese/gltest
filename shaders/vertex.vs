#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec4 inColor;

out vec4 outColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 worldMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * worldMatrix * vec4(position, 1.0);
	outColor = inColor;
}
