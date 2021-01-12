package com.ykhe.airhockeytouch.util;

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

        public Point translate(Vector vector) {
            return new Point(x + vector.x,
                    y + vector.y,
                    z + vector.z);
        }
    }

    //圆形
    public static class Circle {
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        //缩放
        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

    //向量
    public static class Vector {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        //利用勾股定理返回向量的長度
        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        //計算兩個向量的交叉乘积
        public Vector crossProduct(Vector other) {
            return new Vector((y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x));
        }

        //计算两个向量之间的点积
        public float dotProduct(Vector other) {
            return x * other.x
                    + y * other.y
                    + z * other.z;
        }

        //会根据缩放比均匀缩放向量每个分量
        public Vector scale(float scaleFactor) {
            return new Vector(
                    x*scaleFactor,
                    y * scaleFactor,
                    z* scaleFactor);
        }
    }

    public static class Ray {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Sphere {
        public final Point center;
        public final float radius;


        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    public static class Plane{
        public final Point point;
        //法向向量
        public final Vector normal;

        public Plane(Point point, Vector normal) {
            this.point = point;
            this.normal = normal;
        }
    }



    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    public static boolean intersects(Sphere sphere, Ray ray) {
        //确定球心与射线的距离,并检查那个距离是否小于球体半径,如果是,那球体就与射线相交
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    //用向量计算距离 球心到射线
    private static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);

        //计算交叉乘积得到第三个向量,它垂直于p1,p2,且这个向量的长度为前两个向量定义三角形面积的两倍( area *2 )
        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        //计算三角形底长度
        float lengthOfBase = ray.vector.length();

        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;

        return distanceFromPointToRay;
    }




    public static Point intersectionPoint(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetween(ray.point , plane.point);

        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
                /ray.vector.dotProduct(plane.normal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

}
