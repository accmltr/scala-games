#version 330 core
layout(location=0)in vec2 aPos;// Vertex positions

uniform vec2 position;

void main()
{
	gl_Position=vec4(aPos+position,0.,1.);// Z = 0.0, W = 1.0
}