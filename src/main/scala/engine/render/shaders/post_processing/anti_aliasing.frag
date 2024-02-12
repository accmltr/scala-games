#version 330 core

// Anti Aliasing

uniform sampler2D uScreenTexture;
uniform vec2 uTexelSize;

in vec2 vPos;

out vec4 FragColor;

void main()
{
	FragColor=vec4(.9);
}
