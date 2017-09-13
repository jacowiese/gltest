#version 330

uniform sampler2D textureData;

in vec2 outTexCoord;
out vec4 fragColor;

void main() {
	fragColor = texture2D(textureData, outTexCoord).rgba;
//    fragColor = vec4(outTexCoord.x, outTexCoord.y, 1, 1);
}
