
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
	vec2 cornerOrigin=vec2(uWidth-uCornerRadius,uHeight-uCornerRadius);
	vec2 cornerDelta=p-cornerOrigin;
	
	if(p.x<=uWidth&&p.y<=uHeight)// Inside width and height
	{
		if(cornerDelta.x>=0&&cornerDelta.y>=0)// Corner
		{
			FragColor=vec4(0.,.8353,1.,0.);
		}
		else if(p.x<=uWidth-uBorderInnerWidth&&p.y<=uHeight-uBorderInnerWidth)// Main color
		{
			FragColor=uColor;
		}
		else// Inner border
		{
			FragColor=uBorderColor;
		}
	}
	else// Outer border
	{
		if(cornerDelta.x>=0&&cornerDelta.y>=0)// Corner
		{
			FragColor=vec4(.8,0.,1.,0.);
		}
		else// Remainder of border
		{
			FragColor=vec4(.0235,.3804,0.,0.);
		}
	}
}