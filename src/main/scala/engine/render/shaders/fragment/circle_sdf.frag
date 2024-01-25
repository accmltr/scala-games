
#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform float uRadius;

out vec4 FragColor;

void main()
{
	if(length(vPos)<uRadius)
	{
		FragColor=uColor;
	}
	else
	{
		discard;
	}
}