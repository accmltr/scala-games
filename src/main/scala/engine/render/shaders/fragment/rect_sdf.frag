
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
	
	if(p.x<=uWidth&&p.y<=uHeight)// Inside width and height
	{
		FragColor=vec4(0.,.8353,1.,0.);
		
		if(p.x<=uWidth-uBorderInnerWidth&&p.y<=uHeight-uBorderInnerWidth)// Main color
		{
			FragColor=uColor;
		}
		else// Inner border
		{
			FragColor=uBorderColor;
		}
		
		// vec2 corner=i-vec2(uCornerRadius);
		// vec2 c=p-corner;
		// if(c.x>=0&&c.y>=0)
		// {
			// 	FragColor=vec4(.302,1.,0.,0.);
			// 	// float l=length(c);
			// 	// if(l<=uCornerRadius)
			// 	// {
				// 		// 	if(l<=uBorderInnerWidth)
				// 		// 	{
					// 			// 		FragColor=vec4(.6627,.2863,0.,0.);
				// 		// 	}
				// 		// 	else
				// 		// 	{
					// 			// 		FragColor=uBorderColor;
				// 		// 	}
			// 	// }
			// 	// else
			// 	// discard;
		// }
		// else if(p.x<=uWidth-uBorderInnerWidth&&p.y<=uHeight-uBorderInnerWidth)
		// FragColor=uColor;
		// else
		// FragColor=uBorderColor;
	}
	else// Outside width and height. Outer border
	{
		FragColor=vec4(1.,0.,.8157,0.);
	}
}