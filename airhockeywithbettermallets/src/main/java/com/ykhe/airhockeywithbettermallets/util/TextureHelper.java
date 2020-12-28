package com.ykhe.airhockeywithbettermallets.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * author: ykhe
 * date: 20-12-22
 * email: ykhe@grandstream.cn
 * description:
 */
public class TextureHelper {
    private static final String TAG = "TextureHelper";
    //把一个图像文件数据加载到一个OpenGL的 纹理之中
    //返回加载图像后的OpenGL纹理的ID
    public static int loadTexture(Context context,int resourceId){
        final int[] textureObjectIds = new int[1];
        //创建1个纹理对象
        GLES20.glGenTextures(1,textureObjectIds,0);
        //检查glGenTextures调用是否成功
        if (textureObjectIds[0] == 0){
            if (LoggerConfig.ON){
                Log.w(TAG,"Could not generate a new OpenGL texture Object");
            }
            return 0;
        }


        //将图像资源解压缩为一个Android位图
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //使用原始的图像数据,而不是这个图像的缩放版本
        options.inScaled = false;

        //将图像资源解码为bitmap 对象
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resourceId,options);
        if (bitmap == null){
            if (LoggerConfig.ON){
                Log.w(TAG, "" );
            }

            GLES20.glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }

        //以2D纹理对待 绑定到textureObjectIds[0] id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[0]);

        // GL_TEXTURE_MIN_FILTER :对于缩小的情况,使用三线性过滤(GL_LINEAR_MIPMAP_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
        // GL_TEXTURE_MAG_FILTER :对于放大情况,使用双线性过滤(GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        //告诉OpenGL读入bitmap定义的位图数据,并把他复制到当前绑定的纹理对象
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);

        bitmap.recycle();

        //生成MIPMAP贴图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解除纹理绑定,避免其他纹理方法调用意外的改变这个纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);

        return textureObjectIds[0];
    }
}
