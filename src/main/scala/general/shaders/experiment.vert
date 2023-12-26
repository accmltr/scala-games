#version 330 core
layout(location=0)in vec2 aPos;// Vertex position

uniform float aspect;
uniform mat3 transform;

void main()
{
	gl_Position=vec4(transform*vec3(aPos.x,aPos.y*aspect,1.),1.);
}