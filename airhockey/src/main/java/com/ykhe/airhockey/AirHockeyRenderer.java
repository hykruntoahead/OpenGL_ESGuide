package com.ykhe.airhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ykhe.airhockey.util.LoggerConfig;
import com.ykhe.airhockey.util.ShaderHelper;
import com.ykhe.airhockey.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author: ykhe
 * date: 20-11-30
 * email: ykhe@grandstream.cn
 * description:
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    public static final String U_COLOR = "u_Color";
    private int uColorLocation;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    //每个顶点有两个分量　
    private static final int POSITION_COMPONENT_COUNT = 2;
    private final Context context;
    private int program;

    /**
     * Android代码运行在Dalvik上,OpenGL作为本地系统库直接运行在硬件上，那它们之间如何通信？
     * 1.JNI
     * 2.把内存从java堆复制到本地堆
     */
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;

    /**
     * 着色器
     * １．顶点着色器:
     *     生成每个顶点的最终位置，针对每个定点，都会执行一次，
     *     一旦位置确定，OpenGL就可把这些可见定点的集合组装成点，直线及三角形
     * ２. 片段着色器:
     *      为组成点，直线或者三角形每个片段生成最终的颜色，针对每个片段，它都会执行一次
     *      一个片段是一个小的，单一颜色的长方形区域，类似于计算机屏幕上的一个像素
     * 一旦最后的颜色生成了，OpenGL就会把它们写到一块称为帧缓冲区的内存块中，
     * 然后，Android会把这个帧缓冲区显示到屏幕上．
     *
     * OpenGL　管道概述:
     * 读取顶点数据-->执行顶点着色器-->组装图元-->光栅化图元-->执行片段着色器-->写入帧缓冲区-->显示在屏幕上
     */

    AirHockeyRenderer(Context context) {
        this.context = context;
        /* 定义顶点数据 两个三角形组成１个矩形 */
        //OpenGL 会把屏幕映射到[-1,1]
        float[] tableVerticesWithTriangles = {
                //三角形1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                //三角形２
                -0.5f,-0.5f,
                0.5f,-0.5f,
                0.5f,0.5f,

                //中间线
                -0.5f,0f,
                0.5f,0f,

                //两个木槌
                0f,-0.25f,
                0f,0.25f
        };

        vertexData = ByteBuffer
                //分配一块本地内存，不由垃圾回收器管理.因为每个浮点数４个字节，所以分配内存大小为定点数的４倍
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                //告诉字节缓冲区按照本地字节序组织它的内容
                .order(ByteOrder.nativeOrder())
                //得到一个反映底层字节的FloatBuffer类实例，使得无须直接操作字节
                .asFloatBuffer();
        //把数据从Dalvik内存复制到本地内存，当进程结束时，这块内存会被释放掉
        vertexData.put(tableVerticesWithTriangles);
    }

    /**
     * 当Surface 被创建的时候，GLSurfaceView会调用这个方法
     *
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置清空屏幕用的颜色（黑）
        gl.glClearColor(0f, 0f, 0f, 0f);

        //从glsl文件读取着色器代码
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);

        //编译代码生成&返回着色器句柄
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        //将定点着色器和片段着色器链接到一起形成OpenGL程序对象&返回程序句柄
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader);

        if (LoggerConfig.ON){
            ShaderHelper.validateProgram(program);
        }

        //告诉OpenGL 在绘制任何东西到屏幕上的时候要使用这里定义的程序
        GLES20.glUseProgram(program);

        //获取uniform - u_Color位置(res/raw/simple_fragment_shader.glsl中定义)
        uColorLocation = GLES20.glGetUniformLocation(program,U_COLOR);
        //获取attribute - a_Position位置(res/raw/simple_vertex_shader.glsl中定义)
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);

        //保证OpenGL从缓冲区读取时,它会从开头(0)位置开始读取
        vertexData.position(0);

        //告诉OpenGL,可以在缓冲区vertexData中找到a_Position对应的数据(关联属性与顶点数据的数组)
        /**
         * int indx, 这个是属性位置
         * int size, 这是每个属性的数据的计算,或者对于这个属性,有多少个分量与每一个顶点相关联.
         *          我们为每个顶点只传递了两个分量,但是在着色器中,a_Position被定义为vec4,它有4个分量.
         *          如果一个分量没有被指定值,默认情况下,OpenGL会把前3个分量设为0,最后一个分量设为1
         * int type, 数据的类型
         * boolean normalized, 只有使用整型数据的时候,该参数才有意义
         * int stride, 只有当一个数组存储多于一个属性时,它才有意义.当前只有一个属性,可忽略,传0
         * java.nio.Buffer ptr 这个参数告诉OpenGL去哪里读取数据. 不要忘了它会从缓冲区的当前位置读取,
         *                      若没调用vertexData.position(0),它可能尝试读取缓冲区结尾后面的内容
         *                      导致应用崩溃
         */
        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,0,vertexData);
        //使能顶点数组,这样OpenGL就知道去哪里寻找它所需的数据了
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    /**
     * 在Surface被创建以后，每次Surface尺寸变换时，这个方法都会被GLSurfaceView调用到
     * 在横屏\竖屏切换时，Surface尺寸发生变化
     *
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口viewport尺寸
        gl.glViewport(0, 0, width, height);
    }

    /**
     * 当绘制一帧时，这个方法会被GLSurfaceView调用.
     * 在这个方法中，一定要绘制一些东西，即使只是清空屏幕．
     * 因在此方法返回后，渲染缓冲区会被交换并显示在屏幕上，
     * 如果什么都没画，可能会闪烁
     *
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //清空屏幕
        //擦除屏幕上所有颜色，并用之前glClearColor()调用定义的颜色填充整个屏幕
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // 绘制球桌
        // 更新着色器代码中的u_color值.
        // 与属性不同,uniform没有默认值.所以我们提供4个分量(r:1;g:1;b:1;a:1)绘制为白色
        GLES20.glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
        // 绘制2个三角形,从数组0位置开始读取6个顶点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);

        //绘制中间线
        //红色
        GLES20.glUniform4f(uColorLocation,1.0f,0f,0f,1.0f);
        //从数组6位置开始读取2个顶点 绘制一条线
        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        //把木垂绘制为点 蓝色
        GLES20.glUniform4f(uColorLocation,0f,0f,1f,1f);
        gl.glDrawArrays(GL10.GL_POINTS,8,1);

        //把木垂绘制为点 红色
        GLES20.glUniform4f(uColorLocation,1f,0f,0f,1f);
        gl.glDrawArrays(GL10.GL_POINTS,9,1);
         
    }
}
