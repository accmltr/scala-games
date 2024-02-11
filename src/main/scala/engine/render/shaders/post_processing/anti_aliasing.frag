#version 330 core

// Anti Aliasing

uniform sampler2D uScreenTexture;
uniform vec2 uTexelSize;

in vec2 vPos;

out vec4 FragColor;

void main()
{
	FragColor=vec4(.9);
	// FragColor=texture(uScreenTexture,vPos);
	// vec2 TexCoord=vPos.xy;
	// vec2 offset=vec2(1.,1.)*uTexelSize;
	// vec4 color=texture(uScreenTexture,TexCoord);
	// vec4 sum=vec4(0.);
	// sum+=texture(uScreenTexture,TexCoord+vec2(-1.,-1.)*offset)*.25;
	// sum+=texture(uScreenTexture,TexCoord+vec2(1.,-1.)*offset)*.25;
	// sum+=texture(uScreenTexture,TexCoord+vec2(-1.,1.)*offset)*.25;
	// sum+=texture(uScreenTexture,TexCoord+vec2(1.,1.)*offset)*.25;
	// float distance=length(sum-color);
	// FragColor=vec4(distance);
}
