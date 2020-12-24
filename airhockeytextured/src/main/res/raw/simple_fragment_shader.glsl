//片段着色器
//精度限定　中等精度
precision mediump float;
//如果片段属于一条直线,那么OpenGL就会用构成那条直线的两个顶点计算混合后的颜色;
//如果那个片段属于一个三角形,那OpenGL就会用构成那个三角形的三个顶点计算其混合后的颜色.
varying vec4 v_Color;
void main(){
    gl_FragColor = v_Color;
}