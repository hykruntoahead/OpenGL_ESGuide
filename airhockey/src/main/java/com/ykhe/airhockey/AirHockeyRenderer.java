package com.ykhe.airhockey;

import android.opengl.GLSurfaceView;

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
    //每个顶点有两个分量　
    private static final int POSITION_COMPONENT_COUNT = 2;

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

    AirHockeyRenderer() {
        //定义顶点数据 两个三角形组成１个矩形
        float[] tableVerticesWithTriangles = {
                //三角形1
                0f, 0f,
                9f, 14f,
                0f, 14f,

                //三角形２
                0f,0f,
                9f,0f,
                9f,14f,

                //中间线
                0f,7f,
                9f,7f,

                //两个木槌
                4.5f,2f,
                4.5f,12f
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
        //设置清空屏幕用的颜色（红）
        gl.glClearColor(1.0f, 0f, 0f, 0f);
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
    }
}
