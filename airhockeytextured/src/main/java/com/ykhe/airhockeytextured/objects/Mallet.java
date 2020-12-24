package com.ykhe.airhockeytextured.objects;

import android.opengl.GLES20;

import com.ykhe.airhockeytextured.Constants;
import com.ykhe.airhockeytextured.data.VertexArray;
import com.ykhe.airhockeytextured.programs.ColorShaderProgram;

/**
 * author: ykhe
 * date: 20-12-22
 * email: ykhe@grandstream.cn
 * description:木槌
 */
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)
            * Constants.BYTES_PER_FLOAT;

    public static final float[] VERTEX_DATA = {
            // x,y,r,g,b
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f,
    };

    private final VertexArray vertexArray;

    public Mallet(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,2);
    }
}
