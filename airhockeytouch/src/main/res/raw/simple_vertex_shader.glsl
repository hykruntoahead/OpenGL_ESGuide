//顶点着色器－为每个顶点生成最终位置

//对于我们定义过的每个单一顶点，定点着色器都会被调用一次;
//当它被调用的时候,它会在a_Position属性里接收当前顶点的位置，
//这个属性被定义成vec4类型

//mat-4 代表4x4的矩阵
uniform mat4 u_Matrix;

//关键字　attribute就是把属性放进着色器的手段
attribute vec4 a_Position;

void main(){
    gl_Position = u_Matrix * a_Position;
}