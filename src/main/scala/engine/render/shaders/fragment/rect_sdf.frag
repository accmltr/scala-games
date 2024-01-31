
#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform float uWidth;
uniform float uHeight;
uniform float uCornerRadius;

out vec4 FragColor;

void main(){
	
	vec2 p=abs(vPos);
	
	vec2 o=vec2(uWidth/2,uHeight/2);
	vec2 r=vec2(uCornerRadius);
	vec2 i=o-r;
	if(p.x<i.x&&p.y<i.y){
		FragColor=uColor;
	}
	else{
		vec2 pl=p-i;
		if(p.x<o.x&&p.y<o.y){
			if(p.x>0&&p.y>0){
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
	
}