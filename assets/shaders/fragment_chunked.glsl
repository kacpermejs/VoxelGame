#version 330

in float passBasicLight;
//in vec3 passTexCoord;
in vec2 passTexCoord;

out vec4 outColour;

//uniform sampler2DArray textureArray;
uniform sampler2D u_texture;

void main() {
    //outColour = passBasicLight * texture(textureArray, passTexCoord);
    outColour = passBasicLight * texture(u_texture, passTexCoord);
    if (outColour.a == 0) {
        discard;
    }
}