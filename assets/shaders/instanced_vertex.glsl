#version 330 core

layout(location = 0) in vec3 a_position;
layout(location = 1) in vec2 a_texCoord0;

out vec2 v_texCoord;

uniform mat4 u_projTrans;
uniform mat4 u_modelViewProjection;

void main() {
    v_texCoord = a_texCoord0;
    gl_Position = u_projTrans * u_modelViewProjection * vec4(a_position, 1.0);
}