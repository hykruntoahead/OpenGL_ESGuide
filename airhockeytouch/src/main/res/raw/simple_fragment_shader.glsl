//片段着色器
//精度限定　中等精度
precision mediump float;
uniform vec4 u_Color;
void main(){
    gl_FragColor = u_Color;
}