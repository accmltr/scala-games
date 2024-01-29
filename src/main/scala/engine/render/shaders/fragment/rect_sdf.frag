
#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform float uWidth;
uniform float uHeight;
uniform float uCornerRadius;

out vec4 FragColor;

void main(){
	vec2 pos=abs(vPos.xy)
	vec2 furthest=vec2(uWidth,uHeight)
	vec2 cornerCenter=furthest-vec2(uCornerRadius)
	vec2 radVec=pos
	if(pos.x>furthest.x||pos.y>furthest.y)
	discard;
	else if(radVec.x>0&&radVec.y>0&&length(radVec)>uRadius)
	discard;
	else
	FragColor=u_Color
	
}