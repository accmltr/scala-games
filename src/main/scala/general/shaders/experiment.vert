#version 330 core
layout(location=0)in vec2 aPos;// Vertex positions

uniform float aspect;
uniform vec2 position;

void main()
{
	gl_Position=vec4(aPos.x+position.x,aPos.y*aspect+position.y,0.,1.);
}