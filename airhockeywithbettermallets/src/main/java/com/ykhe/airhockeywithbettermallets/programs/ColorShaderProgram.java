package com.ykhe.airhockeywithbettermallets.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.ykhe.airhockeywithbettermallets.R;


/**
 * author: ykhe
 * date: 20-12-23
 * email: ykhe@grandstream.cn
 * description:
 */
public class ColorShaderProgram extends ShaderProgram{
    //uniform locations
    private final int uMatrixLocation;

    //attribute location
    private final int aPositionLocation;
    private final int uColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader,
                R.raw.simple_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);

        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);

        uColorLocation = GLES20.glGetUniformLocation(program,U_COLOR);
    }

    public void setUniforms(float[] matrix,float r, float g, float b){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES20.glUniform4f(uColorLocation,r,g,b,1f);
    }


    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }


}
