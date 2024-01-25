#version 330 core
layout(location=0)in vec2 aPos;// Vertex position

uniform vec2 uRes;
uniform mat3 uTrans;

out vec2 vPos;

void main(){
	vec3 pos=uTrans*vec3(aPos,1.);
	gl_Position=vec4((pos.x/pos.z)/(uRes.x/uRes.y),(pos.y/pos.z),1.,1.);
	vPos=aPos;
}