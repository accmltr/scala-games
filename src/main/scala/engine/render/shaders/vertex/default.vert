#version 330 core
layout(location=0)in vec2 aPos;// Vertex position

uniform vec2 uRes;
uniform mat3 uTrans;

void main(){
	vec3 pos=uTrans*vec3(aPos,1.);
	gl_Position=vec4((uRes.y*pos.x)/(pos.z*uRes.x),(pos.y/pos.z),1.,1.);
}