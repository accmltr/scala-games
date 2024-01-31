
#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform float uWidth;
uniform float uHeight;
uniform float uCornerRadius;
// Border
uniform float uBorderInnerWidth;
uniform float uBorderOuterWidth;
uniform vec4 uBorderColor;

out vec4 FragColor;

void main(){
	
	vec2 p=abs(vPos);
	
	vec2 o=vec2(uWidth/2,uHeight/2);
	vec2 i=o-vec2(uCornerRadius);
	if(p.x<o.x&&p.y<o.y){
		vec2 pl=p-i;
		if(pl.x>=0&&pl.y>=0){
			if(length(pl)<=uCornerRadius)
			FragColor=uColor;
			else
			discard;
		}
		else
		FragColor=uColor;
	}
	else
	discard;
}