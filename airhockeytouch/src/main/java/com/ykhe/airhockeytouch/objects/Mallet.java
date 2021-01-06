package com.ykhe.airhockeytouch.objects;



import com.ykhe.airhockeytouch.data.VertexArray;
import com.ykhe.airhockeytouch.programs.ColorShaderProgram;
import com.ykhe.airhockeytouch.util.Geometry;

import java.util.List;


/**
 * author: ykhe
 * date: 20-12-22
 * email: ykhe@grandstream.cn
 * description:木槌
 */
public class Mallet {
    public static final int POSITION_COMPONENT_COUNT = 3;

    private static final int FLOATS_PRE_VERTEX = 3;

    public final float radius,height;


    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius,float height,int numPointsAroundMallet){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0f,0f,0f),radius,height,numPointsAroundMallet);
        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0,colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,0);
    }

    public void draw(){
       for (ObjectBuilder.DrawCommand drawCommand:drawList){
           drawCommand.draw();
       }
    }
}
