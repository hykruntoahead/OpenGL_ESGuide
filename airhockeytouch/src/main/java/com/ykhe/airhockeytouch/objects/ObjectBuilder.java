package com.ykhe.airhockeytouch.objects;

import android.opengl.GLES20;


import com.ykhe.airhockeytouch.util.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ykhe
 * date: 20-12-25
 * email: ykhe@grandstream.cn
 * description:物体构建器
 */
public class ObjectBuilder {
    interface DrawCommand{
        void draw();
    }

    static class GeneratedData{
        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData,List<DrawCommand> drawList){
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private static final int FLOATS_PRE_VERTEX = 3;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
    private int offset = 0;

    private ObjectBuilder(int sizeInVertices){
        vertexData = new float[sizeInVertices * FLOATS_PRE_VERTEX];
    }


    // 圆柱体顶部顶点数量 --
    // 顶部是一个用三角形扇构造的圆,有一个顶点在圆心,围着圆的每个点都有一个顶点,
    // 并且围着圆的地一个顶点要重复两次才能使圆闭合
    private static int sizeOfCircleInVertices(int numPoints){
        return 1 + (numPoints +1);
    }


    // 圆柱体侧面顶点的数量
    // 一个圆柱体是一个卷起来的长方形,由三角形带构造,围着顶部圆的每个点都需要两个顶点
    // 且前两个顶点要重复两次才能使这个管闭合
    private static int sizeOfOpenCylinderInVertices(int numPoints){
        return (numPoints + 1) *2;
    }

    //创建冰球
    static GeneratedData createPuck(Geometry.Cylinder puck, int numPoints){
        //一个冰球由一个顶部圆和一个圆柱体侧面构成,所以所有顶点数量为它们之和
        int size = sizeOfCircleInVertices(numPoints)
                + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        //圆形顶部需要被放在冰球的顶部 所以为圆心为圆柱中心点向上移动1/2个高度
        Geometry.Circle puckTop = new Geometry.Circle(
                puck.center.translateY(puck.height/2f),
                puck.radius);

        builder.appendCircle(puckTop,numPoints);
        builder.appendOpenCylinder(puck,numPoints);

        return builder.build();
    }


    //创建木槌

    /**
     * 底部一个大低圆柱状底盘
     * 上面一个小高圆柱状手柄
     */
    static GeneratedData createMallet(Geometry.Point center,float radius,
                                      float height,int numPoints){
        int size = sizeOfCircleInVertices(numPoints) *2 +
                sizeOfOpenCylinderInVertices(numPoints) *2;

        ObjectBuilder builder = new ObjectBuilder(size);

        float baseHeight = height * 0.25f;

        Geometry.Circle baseCircle = new Geometry.Circle(
                center.translateY(-baseHeight),
                radius);

        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(
                baseCircle.center.translateY(-baseHeight /2f),
                radius,baseHeight);

        builder.appendCircle(baseCircle,numPoints);
        builder.appendOpenCylinder(baseCylinder,numPoints);

        float handleHeight = height * 0.75f;
        float handleRadius = radius/3f;

        Geometry.Circle handleCircle = new Geometry.Circle(center.translateY(height*0.5f),
                handleRadius);
        Geometry.Cylinder handleCylinder = new Geometry.Cylinder(
                handleCircle.center.translateY(-handleHeight/2f),
                handleRadius,handleHeight);

        builder.appendCircle(handleCircle,numPoints);
        builder.appendOpenCylinder(handleCylinder,numPoints);

        return builder.build();
    }


    private void appendCircle(Geometry.Circle circle,int numPoints){
        final int startVertex = offset / FLOATS_PRE_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        //三角形扇的中心点
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        //围绕circle.center定义的圆心点按扇形展开,并把第一个点绕圆周重复两次考虑在内.

        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = ((float) i / (float) numPoints)
                    * ((float) Math.PI * 2f);


            vertexData[offset++] = circle.center.x +
                    circle.radius * (float) Math.cos(angleInRadians);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z +
            +circle.radius * (float) Math.sin(angleInRadians);
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,startVertex,numVertices);
            }
        });

    }

    private void appendOpenCylinder(Geometry.Cylinder cylinder,int numPoints){
        final int startVertex = offset / FLOATS_PRE_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);

        final float yStart = cylinder.center.y - (cylinder.height/2);
        final float yEnd = cylinder.center.y + (cylinder.height/2);

        //生成三角形带
        /**
         *  p1       p3
         *  | \      |
         *  |  \     |
         *  |    \   |
         *  |      \ |
         *  p2 ----- p4
         */
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = ((float) i / (float) numPoints)
                    * ((float) Math.PI * 2f);

            float xPosition = cylinder.center.x
                    +cylinder.radius * (float)Math.cos(angleInRadians);
            float zPosition = cylinder.center.z
                    + cylinder.radius * (float) Math.sin(angleInRadians);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                //绘制三角形带(GL_TRIANGLE_STRIP)
              GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,startVertex,numVertices);
            }
        });
    }


    private GeneratedData build(){
        return new GeneratedData(vertexData,drawList);
    }

}
