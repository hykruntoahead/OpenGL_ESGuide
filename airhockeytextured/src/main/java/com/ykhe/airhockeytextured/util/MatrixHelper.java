package com.ykhe.airhockeytextured.util;

/**
 * author: ykhe
 * date: 20-12-15
 * email: ykhe@grandstream.cn
 * description:
 */
public class MatrixHelper {

    public static void perspectiveM(float[] m,float yFovInDegrees,float aspect,float n, float f){
        /**
         *  什么是投影？
         *
         *  计算机显示器是一个二维表面，所以如果你想显示三维图像，你需要一种方法把3D几何体转换成一种可作为二维图像渲染的形式.
         *
         *  投影矩阵推导见:https://blog.csdn.net/stl112514/article/details/83927643
         *
         * 投影矩阵:
         * --                                      --
         * | a/aspect   0        0         0        |
         * |                                        |
         * |    0      a         0         0        |
         * |                                        |
         * |    0      0   -(f+n)/(f-n)  -2fn/(f-n) |
         * |                                        |
         * |    0      0        -1         0        |
         * |                                        |
         * --                                      --
         *
         * a 相机的焦距 由 1/tan(视野/2)计算得到,必须小于180度
         * aspect 屏幕的宽高比
         * f 到远处平面的距离,必须是正值且大于到近处平面的距离
         * n 到近处平面的距离,必须是正值.比如,如果此值被设为1,那近处平面就位于一个z值为-1处
         */
        //计算焦距 - 基于在y轴上的视野
        final float angleInRadians = (float)(yFovInDegrees * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        //输出矩阵

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -(f+n)/(f-n);
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -(2f*f*n)/(f-n);
        m[15] = 0f;
    }

}
