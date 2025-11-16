package tools;

import tools.Color;
import tools.Hit;
import tools.Vec3;

public class Plane {
    private final Vec3 center;
    private final double radius;
    private final Color color;
    private final double yMin;

    public CurvedPlane(Vec3 center, double radius, Color color, double yMin){
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.yMin = yMin;
    }

    //检测射线与弧形平面（球面上半部分）的相交
    public Hit hit(Ray ray){
        // 球面方程：(p - center)·(p - center) = radius²
        // 射线方程：p = ray.origin + t*ray.direction（t>0）
        Vec3 oc = Vec3.subtract(ray.oragin(), center);
        double a = Vec3.dot(ray.direction(), ray.direction());
        double b = 2 * Vec3.dot(ray.direction(), ray.direction());
        double c = Vec3.dot(oc,oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null;
        }

        //两个解
        double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t = -1;

        //选择有效交点（t>0 且位于yMin上方，模拟地面部分）
        if (t1 > 0) {
            Vec3 p1 = Vec3.add(ray.origin(), Vec3.multiply(t1, ray.direction()));
            if (p1.y() >= yMiin) {
                t = t1;
            }
        }
        if (t < 0 && t2 > 0) {
            Vec3 p2 = Vec3.add(ray.origin(), Vec3.multiply(t2, ray.direction()));
            if (p2.y() >= yMiin) {
                t = t2;
            }
        }

        if (t < 0) {
            return null;
        }

        //计算交点和法向量（球面法向量，从球心指向交点）
        Vec3 position = Vec3.add(ray.origin(), Vec3.multiply(t, ray.direction()));
        Vec3 normal = Vec3.normalize(Vec3.subtract(position, center));
        return new Hit(t, position, normal, color);
    }

    public Color color() {
        return color;
    }
}
