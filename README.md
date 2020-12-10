# OpenGL_ESGuide
OpenGL ES　应用开发实践指南Android


学习＜OpenGL ES　应用开发实践指南　Android卷＞（美）KevinBrothaler著



## 第1章 准备开始

- 创建GLSurfaceView实例
- 检查系统是否支持OpenGLES  2.0
- 为Open GL2.0配置渲染表面
- 在Acticity 对应生命周期关联glsurfaceview [onPause,onResume]
- 创建渲染器 Renderer类
  - onSurfaceCreate
  - onSurfaceChanged
  - onDrawFrame 
  
  
  
  
## 第2章 定义顶点和着色器

### 定义顶点  

OpenGL里能绘制的图元:点,直线及三角形  

- 添加长方形桌子顶点,由两个三角形组成
- 添加中间线和两个木槌顶点

### 顶点被OpenGL 存取
  
```
 vertexData = ByteBuffer
                //分配一块本地内存，不由垃圾回收器管理.因为每个浮点数４个字节，所以分配内存大小为定点数的４倍
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                //告诉字节缓冲区按照本地字节序组织它的内容
                .order(ByteOrder.nativeOrder())
                //得到一个反映底层字节的FloatBuffer类实例，使得无须直接操作字节
                .asFloatBuffer();
        //把数据从Dalvik内存复制到本地内存，当进程结束时，这块内存会被释放掉
 vertexData.put(tableVerticesWithTriangles);

```

### 引入OpenGL 管道

- 顶点着色器
- 片段着色器

#### 创建顶点着色器
着色器使用 GLSL定义:GLSL是OpenGL的着色语言,语言结构与C语言类似
- attribute 把属性放进着色器的手段
#### 创建第一个片段着色器
- 精度限定符 lowp mediump highp
- 生成片段颜色 uniform 

**着色器只是可以运行在GPU上的特殊类型的程序**

### OpenGL颜色模型
OpenGL 使用累加**RGB**颜色模型



## 第3章 编译着色器及在屏幕上绘制

### 加载着色器

#### 从资源中加载文本
- 文件流读取glsl代码 转为String
#### 读入着色器代码

### 加载着色器

#### 创建一个新着色器对象
- glCreateShader 创建对象 返回一个整型值
- 这个整型号值就是OpenGL对象的引用
- 返回0代表对象创建失败

#### 上传和编译着色器源代码
- glShaderSource(shaderObjectId,shaderCode)  把着色器源代码上传到着色器对象里
- .glCompileShader(shaderObjectId)  编译这个着色器

#### 取出编译状态
```
 //检查编译成功/失败,将结果存入数组第0个元素
final int[] compileStatus = new int[1];
GLES20.glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);

```
#### 取出着色器信息日志
- GLES20.glGetShaderInfoLog(shaderObjectId)

#### 验证编译状态并返回着色器对象ID

查看编译是否成功:
```
//结果为0表示编译失败
if (compileStatus[0] == 0){
  //如果失败,删除shader对象
   GLES20.glDeleteShader(shaderObjectId);

if (LoggerConfig.ON){
      Log.w(TAG, "compilation of shader failed");
}
return 0;
```
不成功返回0,成功返回着色器id

### 把着色器链接进OpenGL的程序
#### 理解OpenGL的 程序
简单来说: 一个OpenGL程序就是把一个顶点着色器和一个片段着色器链接在一起变成单个对象

#### 新建程序对象并附上着色器
- 新建程序对象 glCreateProgram() 返回int型 programObjectId
- 附上着色器: 把顶点和片段着色器都附加到程序对象上
   - GLES20.glAttachShader(programObjectId,vertexShaderId);
   - GLES20.glAttachShader(programObjectId,fragmentShaderId);
   
#### 链接程序
- glLinkProgram(programObjectId); 链接程序
- 检查链接成功失败 
```
 //检查链接成功/失败
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);
```
- 输出日志 glGetProgramInfoLog(programObjectId)
- 验证链接状态并返回程序对象id
```
...
if (linkStatus[0] == 0){
            GLES20.glDeleteProgram(programObjectId);

            if (LoggerConfig.ON){
                Log.d(TAG, "linking of program failed.");
            }

            return 0;
        }
        //如果程序链接成功,返回
 return programObjectId;
```

### 做最后拼接
#### 验证OpenGL程序的对象
- 验证程序
```
//验证对应id指定的程序
GLES20.glValidateProgram(programObjectId);

final int[] validateStatus = new int[1];
//用GL_VALIDATA_STATUS作为参数调用glGEtProgramiv()方法检查其结果
GLES20.glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);
Log.d(TAG, "Results of validating program:" + validateStatus[0] +"\nLog:"
                + GLES20.glGetProgramInfoLog(programObjectId));

return validateStatus[0] != 0;
```
- 使用程序 glUseProgram(program)

#### 获得一个uniform位置
```
//获取uniform - u_Color位置(res/raw/simple_fragment_shader.glsl中定义)
uColorLocation = GLES20.glGetUniformLocation(program,U_COLOR);
```
#### 获取属性位置
```
//获取attribute - a_Position位置(res/raw/simple_vertex_shader.glsl中定义)
aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
```
#### 关联属性与顶点数据数组
```
//保证OpenGL从缓冲区读取时,它会从开头(0)位置开始读取
vertexData.position(0);
//告诉OpenGL,可以在缓冲区vertexData中找到a_Position对应的数据(关联属性与顶点数据的数组)
GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,0,vertexData);
```
#### 使能顶点数组
- glEnableVertexAttribArray(aPositionLocation)

### 在屏幕上绘制
#### 绘制桌子
```
 // 绘制球桌 
 // 更新着色器代码中的u_color值.
 // 与属性不同,uniform没有默认值.所以我们提供4个分量(r:1;g:1;b:1;a:1)绘制为白色
 GLES20.glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
 // 绘制2个三角形,从数组0位置开始读取6个顶点
 GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6); 
```
#### 绘制分割线
```
//绘制中间线
//红色
GLES20.glUniform4f(uColorLocation,1.0f,0f,0f,1.0f);
//从数组6位置开始读取2个顶点 绘制一条线
GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

```

#### 把木槌绘制为点
```
//把木垂绘制为点 蓝色
GLES20.glUniform4f(uColorLocation,0f,0f,1f,1f);
gl.glDrawArrays(GL10.GL_POINTS,8,1);

//把木垂绘制为点 红色
GLES20.glUniform4f(uColorLocation,1f,0f,0f,1f);
gl.glDrawArrays(GL10.GL_POINTS,9,1);
```

#### OpenGL 坐标映射屏幕
暂需知道:OpenGL吧下,y坐标映射到屏幕 范围均为[-1,1]
#### 指定点(木槌)大小
- gl_PointSize = 10.0;(in simple_vertex_shader.glsl)


## 第4章 增加颜色和着色

### 平滑着色
#### 平滑着色是在顶点之间完成的

### 引入三角形扇
```
//绘制一个三角形扇:以第一个顶点作为起始,使用相邻两个顶点创建一个三角形,
// 接下来每个顶点都会创建一个三角形,围绕起始的中心点按扇形展开
GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
```

### 增加一个新的颜色属性
```
  /* 定义顶点数据 两个三角形组成１个矩形 */
        //OpenGL 会把屏幕映射到[-1,1]
        float[] tableVerticesWithTriangles = {
                /**
                 * p5----------p4
                 * |            |
                 * |            |
                 * |            |
                 * |     p1     |
                 * |            |
                 * |            |
                 * |            |
                 * p2-----------p3
                 * p6
                 *
                 * x,y,r,g,b
                */
                0f,      0f,          1f,1f,1f,//p1 r g b

                -0.5f,-0.5f,        0.7f,0.7f,0.7f,//p2
                0.5f, -0.5f,        0.7f,0.7f,0.7f,//p3

                0.5f, 0.5f,         0.7f,0.7f,0.7f,//p4
                -0.5f, 0.5f,        0.7f,0.7f,0.7f,//p5
                -0.5f, -0.5f,       0.7f,0.7f,0.7f,//p6

                //中间线
                -0.5f, 0f, 1f,0f,0f,
                0.5f, 0f,  1f,0f,0f,

                //两个木槌
                0f, -0.25f, 0f,0f,1f,
                0f, 0.25f,  1f,0f,0f,
        };
```
#### 给着色器增加颜色属性
修改 simle_vertex_shader.glsl 更新新的着色器代码
``` 
attribute vec4 a_Position;
attribute vec4 a_Color; 
varying vec4 v_Color;

void main(){ 
    v_Color = a_Color;

    gl_Position = a_Position; 
    gl_PointSize = 10.0;
}
```
- varying:是一个特殊的变量类型,它把给它的那些值进行混合,并把这些混合后的值发送给片段着色器
把varying 加入片段着色器
```
precision mediump float; 
varying vec4 v_Color;
void main(){
    gl_FragColor = v_Color;
}
```
用varying变量v_Color替换了原来代码中的uniform:
- 如果片段属于一条直线,那么OpenGL就会用构成那条直线的两个顶点计算混合后的颜色;
- 如果那个片段属于一个三角形,那OpenGL就会用构成那个三角形的三个顶点计算其混合后的颜色.

#### 一个varying如何生成每个片段上混合后的颜色?
- 沿着一条直线做线性插值(长度比例确定颜色相对权重)
![](pic/line_blended.png)

- 在一个三角形表面上混合(面积比例确定颜色相对权重)
![](pic/triangle_blended.png)


### 用新的颜色属性渲染
#### 更新常量
更新AColor相关常量 及步长stride
```
private static final String A_COLOR = "a_Color";
private static final int COLOR_COMPONENT_COUNT = 3;

private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
private int aColorLocation;

```
#### 更新onSurfaceCreated()
```
...
//获取新的a_Color属性
aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
...
//更新glVertexAttribPointer()调用,加入跨距:
 GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,STRIDE,vertexData);
 ...
 
 //把顶点数据与着色器中的a_Color关联起来
vertexData.position(POSITION_COMPONENT_COUNT);
GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,STRIDE,vertexData);
GLES20.glEnableVertexAttribArray(aColorLocation);
...
```

#### 更新onDrawFrame
- glUniform4f()调用
