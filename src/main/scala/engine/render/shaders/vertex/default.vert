#version 330 core
layout(location=0)in vec2 aPos;// Vertex position in pixel coordinates

uniform vec2 uRes;// Viewport resolution in pixels
uniform mat3 uTrans;// Transformation matrix

out vec2 vPos;// Vertex position in pixel coordinates, to be interpolated

void main(){
	// Convert coordinates for OpenGL and apply transformation
	vec2 pos=((uTrans*vec3((aPos),1.)).xy/uRes)*2.-1.;
	gl_Position=vec4(pos.x,pos.y,0.,1.);
	vPos=aPos;// Pass the given vertex position to the fragment shader
}