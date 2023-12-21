#version 330 core
out vec4 FragColor;

uniform vec2 position;
uniform float radius;// Assuming radius is a single float value
uniform vec2 resolution;

void main()
{
	vec2 uv=(gl_FragCoord.xy-position)/resolution;
	float len=length(uv);
	FragColor=vec4(uv,len,1.);
}