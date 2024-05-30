#version 330 core
in vec4 a_position;
in vec2 a_texCoord0;

uniform mat4 u_projTrans;

out vec2 v_texCoord;

vec2 texCoords[4] = vec2[4](
    vec2(0.0f, 0.0f),
    vec2(1.0f, 0.0f),
    vec2(1.0f, 1.0f),
    vec2(0.0f, 1.0f)
);

void main() {
    v_texCoord = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}