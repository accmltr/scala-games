#version 330 core

// Anti Aliasing

layout(location=0)in vec3 aPos;

uniform sampler2D uScreenTexture;
uniform vec2 uTexelSize;

in vec2 TexCoord;

out vec4 FragColor;

void main()
{
	vec2 offset=vec2(1.,1.)*uTexelSize;
	vec4 color=texture(uScreenTexture,TexCoord);
	vec4 sum=vec4(0.);
	sum+=texture(uScreenTexture,TexCoord+vec2(-1.,-1.)*offset)*.25;
	sum+=texture(uScreenTexture,TexCoord+vec2(1.,-1.)*offset)*.25;
	sum+=texture(uScreenTexture,TexCoord+vec2(-1.,1.)*offset)*.25;
	sum+=texture(uScreenTexture,TexCoord+vec2(1.,1.)*offset)*.25;
	float distance=length(sum-color);
	FragColor=vec4(distance);
}
