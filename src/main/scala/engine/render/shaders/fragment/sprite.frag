#version 330 core

in vec2 vPos;

uniform vec4 uColor;
uniform sampler2D uTexture;
uniform int uWidth;
uniform int uHeight;

out vec4 FragColor;

void main()
{
	// FragColor=vec4(pixelIndex,0.,1.);
	// FragColor=vec4(pixelIndex/vec2(uWidth,uHeight),0.,1.);
	FragColor=texture(uTexture,vPos/vec2(uWidth,uHeight));
}