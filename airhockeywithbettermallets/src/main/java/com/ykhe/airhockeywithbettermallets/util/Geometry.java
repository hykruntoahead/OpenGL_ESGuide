package com.ykhe.airhockeywithbettermallets.util;

/**
 * author: ykhe
 * date: 20-12-25
 * email: ykhe@grandstream.cn
 * description: 几何图形
 */
public class Geometry {

    /**
     * 三维场景中的一个点
     */
    public static class Point {
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        //沿着y轴平移
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }
    }

    //圆形
    public static class Circle{
        public final Point center;
        public final float radius;

        public Circle(Point center,float radius){
            this.center = center;
            this.radius = radius;
        }

        //缩放
        public Circle scale(float scale){
            return new Circle(center,radius * scale);
        }
    }

    public static class Cylinder{
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center,float radius,float height){
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

}
