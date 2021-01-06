package com.ykhe.airhockeytouch.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.ykhe.airhockeytouch.R;


/**
 * author: ykhe
 * date: 20-12-22
 * email: ykhe@grandstream.cn
 * description:纹理着色程序
 */
public class TextureShaderProgram extends ShaderProgram {
    //Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context){
        super(context, R.raw.texture_vertex_shader,
                R.raw.texture_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program,U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program,A_TEXTURE_COORDINATES);
    }

    //传递矩阵和纹理给它们的uniform
    public void setUniforms(float[] matrix,int textureId){
        //将矩阵传递到着色器程序中
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        //将“活动纹理单位”设定为“纹理单位0”
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //将纹理绑定到此单位
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);

        //告诉“纹理统一采样器”在着色器中使用该纹理，方法是告诉它从纹理单元0读取
        // 把被选定的纹理单元传递给片段着色其中的u_TextureUnit
        GLES20.glUniform1i(uTextureUnitLocation,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation(){
        return aTextureCoordinatesLocation;
    }



}
