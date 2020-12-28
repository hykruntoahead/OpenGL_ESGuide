package com.ykhe.airhockeywithbettermallets;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;


import com.ykhe.airhockeywithbettermallets.objects.Mallet;
import com.ykhe.airhockeywithbettermallets.objects.Puck;
import com.ykhe.airhockeywithbettermallets.objects.Table;
import com.ykhe.airhockeywithbettermallets.programs.ColorShaderProgram;
import com.ykhe.airhockeywithbettermallets.programs.TextureShaderProgram;
import com.ykhe.airhockeywithbettermallets.util.MatrixHelper;
import com.ykhe.airhockeywithbettermallets.util.TextureHelper;

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
    //投影矩阵
    private final float[] projectionMatrix = new float[16];
    //模型矩阵
    private final float[] modelMatrix = new float[16];
    //视图矩阵
    private final float[] viewMatrix = new float[16];

    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private Table table;
    private Mallet mallet;
    private Puck puck;

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
        //围着圆的32个点创建
        mallet = new Mallet(0.08f,0.15f,32);
        puck = new Puck(0.06f,0.02f,32);

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
        /**
         * float[] rm,       目标数组.该矩阵的长度应该至少容纳16个元素,以便能存储视图矩阵
         * int rmOffset,     setLookAtM()会把结果从rm的这个偏移值开始存入rm
         * float eyeX, --
         * float eyeY,   |-->眼睛所在为位置. 场景中的所有东西看起来都像是从这个点观察它们一样
         * float eyeZ, --
         * 将眼睛eye位置设为(0,1.2,2.2):意味着眼睛位于x-z平面的1.2个单位,并向后2.2个单位
         *
         * float centerX, --
         * float centerY,  |--> 这是眼睛正在看的地方;这个位置出现在整个场景的中心
         * float centerZ, --
         * 把中心center设为(0,0,0):意味将向下看你前面的原点
         *
         * float upX, --
         * float upY,  |--> 眼睛的朝向,也即可以看做头部指向的地方.upY的值为1意味着头部笔直指向上方
         * float upZ  --
         * 把指向up设为(0,1,0):意味着你的头是笔直指向上面的,这个场景不会旋转到任何一边
         */
        Matrix.setLookAtM(viewMatrix,0,0f,1.2f,2.2f,
                 0f,0f,0f,0f,1f,0f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //将投影和视图矩阵乘在一起的结果缓存到viewProjectionMatrix.
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,
                0,viewMatrix,0);


        //绘制桌子
        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix,texture);
        table.bindData(textureProgram);
        table.draw();

        //绘制木槌1
        positionObjectInScene(0f,mallet.height / 2f, -0.4f);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix,1f,0f,0f);
        mallet.bindData(colorProgram);
        mallet.draw();

        //绘制木槌2
        positionObjectInScene(0f,mallet.height/2f,0.4f);
        colorProgram.setUniforms(modelViewProjectionMatrix,0f,0f,1f);
        mallet.draw();

        //绘制冰球
        positionObjectInScene(0f,puck.height/2f,0f);
        colorProgram.setUniforms(modelViewProjectionMatrix,0.8f,0.8f,1f);
        puck.bindData(colorProgram);
        puck.draw();
    }


    private void positionTableInScene() {
        //球桌原来是以x,y坐标定义的,因此要使它平放在地上,我们需要让它绕x轴向后旋转90度
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.rotateM(modelMatrix,0,-90f,1f,0f,0f);
        Matrix.multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,
                modelMatrix,0);

    }

    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix,0,x,y,z);
        Matrix.multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,
                modelMatrix,0);
    }

}
