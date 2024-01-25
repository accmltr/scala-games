
#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform float uRadius;
uniform float uBorderInnerWidth;
uniform float uBorderOuterWidth;
uniform vec4 uBorderColor;

out vec4 FragColor;

void main()
{
	float dist=length(vPos);
	
	if(dist<uRadius-uBorderInnerWidth)
	FragColor=uColor;
	else if(dist<uRadius+uBorderOuterWidth)
	FragColor=uBorderColor;
	else
	discard;
}