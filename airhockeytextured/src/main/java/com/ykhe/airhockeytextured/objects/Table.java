package com.ykhe.airhockeytextured.objects;

import android.opengl.GLES20;

import com.ykhe.airhockeytextured.Constants;
import com.ykhe.airhockeytextured.data.VertexArray;
import com.ykhe.airhockeytextured.programs.TextureShaderProgram;

/**
 * author: ykhe
 * date: 20-12-22
 * email: ykhe@grandstream.cn
 * description:桌子相关数据
 * */
public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    //跨据计算
    private static final int STRIDE = (POSITION_COMPONENT_COUNT +
            TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    //顶点数据
    private static final float[] VERTEX_DATA = {
            // X, Y, S,T(按y分量相反方向定义 0.1-0.9 裁剪纹理画出中间部分)
            0f,      0f,       0.5f,0.5f, //p1
            -0.5f,-0.8f,        0f ,0.9f,//p2
            0.5f, -0.8f,        1f ,0.9f,//p3
            0.5f, 0.8f,         1f ,0.1f,//p4
            -0.5f, 0.8f,        0f ,0.1f,//p5
            -0.5f, -0.8f,       0f ,0.9f,//p6

    };

    private final VertexArray vertexArray;
    public Table(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram){
        vertexArray.setVertexAttribPointer(0,
                //把位置数据绑定到被引用的着色器属性上
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                //把纹理坐标数据绑定到被引用的着色器属性上
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
    }

}
