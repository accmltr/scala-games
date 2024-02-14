#version 330 core
layout(location=0)in vec2 aPos;

uniform vec2 uRes;

out vec2 vPos;

void main(){
	vec2 pos=aPos*2.-1.;
	gl_Position=vec4(pos,0.,1.);
	vPos=aPos;
}