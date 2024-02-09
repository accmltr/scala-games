
#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform sampler2D uTexture;
uniform vec2 uRes;

out vec4 FragColor;

void main()
{
	vec2 normalizedPos=vPos/uRes;
	// FragColor=texture(uTexture,vec2(.9));
	FragColor=vec4(vPos/uRes,0,1);
}