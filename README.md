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





