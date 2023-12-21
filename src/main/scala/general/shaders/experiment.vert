#version 330 core
layout(location=0)in vec2 aPos;// Vertex positions

out vec4 vertexColor;// Vertex color

void main()
{
	vertexColor=vec4(aPos,1.,1.);// Set the color to red
	gl_Position=vec4(aPos,0.,1.);// Z = 0.0, W = 1.0
}