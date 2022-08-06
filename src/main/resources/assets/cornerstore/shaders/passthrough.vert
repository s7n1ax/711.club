#version 120

void main() {
    // Just pass everything through as is,
    // we don't need to manipulate vertices

    // todo test
    //    gl_TexCoord[0] = gl_MultiTexCoord0;
    //    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

    gl_Position = gl_Vertex;
}