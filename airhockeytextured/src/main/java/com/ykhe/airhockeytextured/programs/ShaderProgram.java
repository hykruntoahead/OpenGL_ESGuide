package com.ykhe.airhockeytextured.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.ykhe.airhockeytextured.util.ShaderHelper;
import com.ykhe.airhockeytextured.util.TextResourceReader;

/**
 * author: ykhe
 * date: 20-12-22
 * email: ykhe@grandstream.cn
 * description:
 */
public class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Shader program
    protected final int program;

    protected ShaderProgram(Context context,int vertexShaderResourceId,
                            int fragmentShaderResourceId){
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId));
    }

    public void useProgram(){
        GLES20.glUseProgram(program);
    }

}
