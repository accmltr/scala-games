#type vertex
#version 330 core
layout(location=0)in vec3 aPos;
layout(location=1)in vec4 aColor;
layout(location=2)in vec2 aTexCoord;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoord;

void main()
{
    fColor=aColor;
    fTexCoord=aTexCoord;
    gl_Position=uProjection*uView*vec4(aPos,1.);
}

#type fragment
#version 330 core
in vec4 fColor;
in vec2 fTexCoord;

uniform float uTime;
uniform sampler2D uTexture;
uniform vec2 uResolution;

out vec4 FragColor;

void main()
{
    // Set color to texture and strech the coord based on the aspect ratio
    vec2 uv=fTexCoord;
    uv.x*=1.-uResolution.x/uResolution.y;
    FragColor=texture(uTexture,uv);
    
}
