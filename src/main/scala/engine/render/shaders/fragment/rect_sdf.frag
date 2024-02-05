
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

bool isInsideRounderRect(vec2 point,vec2 size,float cornerRadius)
{
	vec2 p=abs(point);
	vec2 cornerCenter=size-cornerRadius;
	vec2 cornerDelta=p-cornerCenter;
	float cornerDeltaLength=length(cornerDelta);
	
	if(p.x<=cornerCenter.x&&p.y<=cornerCenter.y)// Inside width and height
	{
		return true;
	}
	else if(p.x<=size.x&&p.y<=size.y)// Outer border
	{
		if(cornerDelta.x>=0&&cornerDelta.y>=0)// Corner
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
	
	vec2 p=abs(vPos);
	vec2 cornerCenter=vec2(uWidth-uCornerRadius,uHeight-uCornerRadius);
	vec2 cornerDelta=p-cornerCenter;
	float cornerDeltaLength=length(cornerDelta);
	
	if(isInsideRounderRect(vPos,vec2(uWidth,uHeight),uCornerRadius))
	FragColor=uColor;
	else
	discard;
	
	// if(p.x<=uWidth&&p.y<=uHeight)// Inside width and height
	// {
		// 	if(cornerDelta.x>=0&&cornerDelta.y>=0)// Corner
		// 	{
			// 		if(cornerDeltaLength<=uCornerRadius-uBorderInnerWidth)// Inner border
			// 		{
				// 			FragColor=vec4(.5451,.251,.6627,0.);
			// 		}
			// 		else if(cornerDeltaLength<=uCornerRadius+uBorderOuterWidth)// Outer border
			// 		{
				// 			FragColor=vec4(.4392,.4235,.6196,0.);
			// 		}
			// 		else// Empty space
			// 		{
				// 			discard;
			// 		}
		// 	}
		// 	else if(p.x<=uWidth-uBorderInnerWidth&&p.y<=uHeight-uBorderInnerWidth)// Main color
		// 	{
			// 		FragColor=uColor;
		// 	}
		// 	else// Inner border
		// 	{
			// 		FragColor=uBorderColor;
		// 	}
	// }
	// else// Outer border
	// {
		// 	if(cornerDelta.x>=0&&cornerDelta.y>=0)// Corner
		// 	{
			// 		if(cornerDeltaLength<=uCornerRadius+uBorderOuterWidth)
			// 		{
				// 			FragColor=vec4(.251,.4157,.6627,0.);
			// 		}
			// 		else// Empty space
			// 		{
				// 			discard;
			// 		}
		// 	}
		// 	else// Remainder of border
		// 	{
			// 		FragColor=vec4(.0235,.3804,0.,0.);
		// 	}
	// }
}