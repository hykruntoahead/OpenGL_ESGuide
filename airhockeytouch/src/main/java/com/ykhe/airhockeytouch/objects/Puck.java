package com.ykhe.airhockeytouch.objects;



import com.ykhe.airhockeytouch.data.VertexArray;
import com.ykhe.airhockeytouch.programs.ColorShaderProgram;
import com.ykhe.airhockeytouch.util.Geometry;

import java.util.List;

/**
 * author: ykhe
 * date: 20-12-28
 * email: ykhe@grandstream.cn
 * description:
 */
public class Puck {
    public static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius,height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height,int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder
                .createPuck(new Geometry.Cylinder(
                        new Geometry.Point(0f,0f,0f),radius,height)
                        ,numPointsAroundPuck);

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;

    }

    public void bindData(ColorShaderProgram colorShaderProgram){
        vertexArray.setVertexAttribPointer(0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,0);
    }

    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
