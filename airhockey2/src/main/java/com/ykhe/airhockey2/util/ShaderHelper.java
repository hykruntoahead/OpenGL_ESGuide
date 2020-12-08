package com.ykhe.airhockey2.util;

import android.opengl.GLES20;
import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;

/**
 * author: ykhe
 * date: 20-12-2
 * email: ykhe@grandstream.cn
 * description: 着色器辅助类
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }


    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        //用glCreateShader创建一个新的着色器对象,并把这个对象的ID存入变量shaderObjectId
        final int shaderObjectId = GLES20.glCreateShader(type);
        //返回值为0代表对象创建失败
        if (shaderObjectId == 0){
            if (LoggerConfig.ON){
                Log.w(TAG,"Could not create new shader.");
            }
            return 0;
        }

        // 把着色器源代码上传到着色器对象里
        // 告诉OpenGL读入字符串shaderCode定义的源代码,并把它与shaderObjectId所引用的着色器对象关联起来
        GLES20.glShaderSource(shaderObjectId,shaderCode);

        // 编译这个着色器
        // 告诉OpenGL编译先前上传到shaderObjectId的源代码
        GLES20.glCompileShader(shaderObjectId);

        //检查编译成功/失败,将结果存入数组第0个元素
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);

        if (LoggerConfig.ON){
            Log.d(TAG, "Results of compiling source:\n"+shaderCode+"\n:"
                    //取出着色器信息日志
                    +GLES20.glGetShaderInfoLog(shaderObjectId));
        }

        //结果为0表示编译失败
        if (compileStatus[0] == 0){
            //如果失败,删除shader对象
            GLES20.glDeleteShader(shaderObjectId);

            if (LoggerConfig.ON){
                Log.w(TAG, "compilation of shader failed");
            }
            return 0;
        }

        return shaderObjectId;
    }


    /**
     * 链接程序
     * @param vertexShaderId
     * @param fragmentShaderId
     * @return
     */
    public static int linkProgram(int vertexShaderId,int fragmentShaderId){
        //新建程序对象(这个程序是指OpenGL中的一个组件)
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0){
            if (LoggerConfig.ON){
                Log.w(TAG, "Could not create new program");
            }
            return 0;
        }


        //附上着色器
        GLES20.glAttachShader(programObjectId,vertexShaderId);
        GLES20.glAttachShader(programObjectId,fragmentShaderId);

        GLES20.glLinkProgram(programObjectId);

        //检查链接成功/失败
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);

        //输入日志
        if (LoggerConfig.ON){
            Log.d(TAG, "Results of linking program:\n"
            +GLES20.glGetProgramInfoLog(programObjectId));
        }

        if (linkStatus[0] == 0){
            GLES20.glDeleteProgram(programObjectId);

            if (LoggerConfig.ON){
                Log.d(TAG, "linking of program failed.");
            }

            return 0;
        }
        //如果程序链接成功,返回
        return programObjectId;
    }

    /**
     * 验证程序
     * @param programObjectId
     * @return
     */
    public static boolean validateProgram(int programObjectId) {
        //验证对应id指定的程序
        GLES20.glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        //用GL_VALIDATA_STATUS作为参数调用glGEtProgramiv()方法检查其结果
        GLES20.glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);
        Log.d(TAG, "Results of validating program:" + validateStatus[0] +"\nLog:"
                + GLES20.glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

}
