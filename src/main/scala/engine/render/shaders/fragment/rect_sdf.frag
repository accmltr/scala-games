
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
	
	vec2 o=.5*vec2(uWidth,uHeight)+vec2(uBorderOuterWidth);
	vec2 i=.5*vec2(uWidth,uHeight)-vec2(uCornerRadius);
	if(p.x<o.x&&p.y<o.y){
		vec2 pl=p-i;
		if(pl.x>=0&&pl.y>=0){
			float l=length(pl);
			if(l<=uCornerRadius){
				if(l<=uCornerRadius-uBorderInnerWidth-uBorderOuterWidth){
					FragColor=vec4(.6627,.2863,0.,0.);
				}
				else
				{
					FragColor=uBorderColor;
				}
			}
			else
			discard;
		}
		else
		{
			if(p.x<o.x-uBorderInnerWidth-uBorderOuterWidth&&p.y<o.y-uBorderInnerWidth-uBorderOuterWidth)
			FragColor=uColor;
			else
			FragColor=uBorderColor;
		}
	}
	else
	discard;
}