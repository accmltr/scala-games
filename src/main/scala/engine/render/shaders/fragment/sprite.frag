
#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform sampler2D uTexture;

out vec4 FragColor;

void main()
{
	FragColor=uColor;
}