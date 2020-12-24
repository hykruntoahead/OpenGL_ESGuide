package com.ykhe.airhockeytextured;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.ykhe.airhockeytextured.objects.Mallet;
import com.ykhe.airhockeytextured.objects.Table;
import com.ykhe.airhockeytextured.programs.ColorShaderProgram;
import com.ykhe.airhockeytextured.programs.TextureShaderProgram;
import com.ykhe.airhockeytextured.util.LoggerConfig;
import com.ykhe.airhockeytextured.util.MatrixHelper;
import com.ykhe.airhockeytextured.util.ShaderHelper;
import com.ykhe.airhockeytextured.util.TextResourceReader;
import com.ykhe.airhockeytextured.util.TextureHelper;

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
    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture;

    protected AirHockeyRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);

        table = new Table();
        mallet = new Mallet();

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context,R.drawable.air_hockey_surface);
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

        //用45度的视野,创建透视投影.z值从-1位置开始到-10位置结束
        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,
                1f,10f);
        //把模型矩阵设为单位矩阵
        Matrix.setIdentityM(modelMatrix,0);
        // 沿着z轴平移-2. 当吧球桌坐标与这个矩阵坐标相乘时,那些坐标最终会沿着z轴负方向移动2个单位
//        Matrix.translateM(modelMatrix,0,0f,0f,-2f);

        //让桌子绕x周旋转60度,造成实际生活中我们站在它面前的效果
        Matrix.translateM(modelMatrix,0,0f,0f,-2.5f);
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f);

        //temp 存储投影矩阵与模型矩阵相乘结果
        final float[] temp = new float[16];
        //调用multiplyMM执行矩阵相乘 结果存入temp
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        //将结果存回projectionMatrix
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);


    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //绘制桌子
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix,texture);
        table.bindData(textureProgram);
        table.draw();

        //绘制木槌
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();
    }
}
