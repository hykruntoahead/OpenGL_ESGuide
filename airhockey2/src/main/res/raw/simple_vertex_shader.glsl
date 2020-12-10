//顶点着色器－为每个顶点生成最终位置

//对于我们定义过的每个单一顶点，定点着色器都会被调用一次;
//当它被调用的时候,它会在a_Position属性里接收当前顶点的位置，
//这个属性被定义成vec4类型

//关键字　attribute就是把属性放进着色器的手段
attribute vec4 a_Position;
attribute vec4 a_Color;
//varying -
//是一个特殊的变量类型,它把给它的那些值进行混合,并把这些混合后的值发送给片段着色器
varying vec4 v_Color;

void main(){
    //通过把a_Color赋值给v_Color,来告诉OpenGL我们需要每个片段都接收一个混合后的颜色.
    v_Color = a_Color;

    gl_Position = a_Position;
    //告诉OpenGL这些点的大小应该是10.
    //(以gl_Position为中心的四边形,这个四边形每边长度与gl_PointSize相等)
    //注意:此处不能写成整型10 否则会报错:: S0001: Type mismatch, cannot convert from 'int' to 'float'
    gl_PointSize = 10.0;
}