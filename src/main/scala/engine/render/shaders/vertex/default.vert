#version 330 core
layout(location=0)in vec2 aPos;// Vertex position in pixel coordinates

uniform vec2 uRes;// Viewport resolution in pixels
uniform mat3 uTrans;// Transformation matrix

out vec2 vPos;// Vertex position in pixel coordinates, to be interpolated

void main(){
	// Convert coordinates to OpenGL and apply transformation
	vec3 pos=uTrans*vec3((aPos/uRes)*2.-1.,1.);
	gl_Position=vec4(pos.xy,0.,1.);
	vPos=aPos;// Pass the given vertex position to the fragment shader
}