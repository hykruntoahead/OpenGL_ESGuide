//顶点着色器－为每个顶点生成最终位置

//对于我们定义过的每个单一顶点，定点着色器都会被调用一次;
//当它被调用的时候,它会在a_Position属性里接收当前顶点的位置，
//这个属性被定义成vec4类型

//关键字　attribute就是把属性放进着色器的手段
attribute vec4 a_Position;
//着色器主要入口点：他所做的就是把前面定义过的位置复制到制定的输出变量gl_Position
void main(){
    gl_Position = a_Position;
}