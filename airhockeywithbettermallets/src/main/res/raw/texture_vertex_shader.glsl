//顶点纹理着色器

uniform mat4 u_Matrix;

attribute vec4 a_Position;

//有两个分量 S T 所以用vec2定义
attribute vec2 a_TextureCoordinates;
//插值
varying  vec2 v_TextureCoordinates;

void main(){
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * a_Position;
}