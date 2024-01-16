#version 330 core
layout(location=0)in vec2 aPos;// Vertex position

uniform float aspect;
uniform mat3 transform;

void main(){
	vec3 pos=transform*vec3(aPos,1.);
	gl_Position=vec4((pos.x/pos.z)/aspect,(pos.y/pos.z),1.,1.);
}