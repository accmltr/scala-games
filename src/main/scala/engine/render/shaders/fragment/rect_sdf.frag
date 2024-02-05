
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
// Border, RectSdf specific
uniform bool uConstantBorderWidth;
uniform bool uAutoDisableCBW;

out vec4 FragColor;

bool isInsideRounderRect(vec2 point,vec2 corner,float cornerRadius)
{
	vec2 p=abs(point);
	vec2 cornerCenter=corner-vec2(cornerRadius);
	vec2 cornerDelta=p-cornerCenter;
	float cornerDeltaLength=length(cornerDelta);
	
	if(p.x<=corner.x&&p.y<=corner.y)
	{
		if(cornerDelta.x>=0&&cornerDelta.y>=0)
		{
			if(cornerDeltaLength<=cornerRadius)
			return true;
			else
			return false;
		}
		return true;
	}
	return false;
}

void main(){
	if(isInsideRounderRect(vPos,vec2(uWidth,uHeight)-vec2(uBorderInnerWidth),uCornerRadius-uBorderInnerWidth))
	FragColor=uColor;
	else if(isInsideRounderRect(vPos,vec2(uWidth,uHeight)+vec2(uBorderOuterWidth),uCornerRadius+uBorderOuterWidth))
	FragColor=uBorderColor;
	else
	discard;
}