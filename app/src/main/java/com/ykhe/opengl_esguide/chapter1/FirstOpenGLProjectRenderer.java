package com.ykhe.opengl_esguide.chapter1;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author: ykhe
 * date: 20-11-30
 * email: ykhe@grandstream.cn
 * description:
 */
public class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {

    /**
     * 当Surface 被创建的时候，GLSurfaceView会调用这个方法
     *
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置清空屏幕用的颜色（红）
        gl.glClearColor(1.0f,0f,0f,0f);
    }

    /**
     * 在Surface被创建以后，每次Surface尺寸变换时，这个方法都会被GLSurfaceView调用到
     * 在横屏\竖屏切换时，Surface尺寸发生变化
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口viewport尺寸
        gl.glViewport(0,0,width,height);
    }

    /**
     * 当绘制一帧时，这个方法会被GLSurfaceView调用.
     * 在这个方法中，一定要绘制一些东西，即使只是清空屏幕．
     * 因在此方法返回后，渲染缓冲区会被交换并显示在屏幕上，
     * 如果什么都没画，可能会闪烁
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //清空屏幕
        //擦除屏幕上所有颜色，并用之前glClearColor()调用定义的颜色填充整个屏幕
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }
}
