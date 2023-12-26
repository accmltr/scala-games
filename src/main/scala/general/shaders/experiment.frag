#version 330 core
out vec4 FragColor;

uniform vec4 tint;
uniform mat3 transform;

void main()
{
	vec3 color=transform[0];// Use the first row of the transformation matrix as the color
	FragColor=vec4(color,1.);// Set the color with an alpha of 1.0
}

// void main()
// {
	// 	FragColor=tint;
// }