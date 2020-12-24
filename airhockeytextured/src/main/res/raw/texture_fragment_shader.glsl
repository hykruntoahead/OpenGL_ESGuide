//片段纹理着色器

precision mediump float;
// u_TextureUnit 接受实际的纹理数据
// sampler2D 指一个二维纹理数据的数组
uniform sampler2D u_TextureUnit;
//纹理坐标
varying vec2 v_TextureCoordinates;

void main(){
    //被插值的纹理坐标和纹理数据被传递给着色器函数texture2D(),他会读入纹理中的那个特定坐标出的颜色值,
    //把结果赋值给gl_FragColor设置片段的颜色
    gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinates);
}