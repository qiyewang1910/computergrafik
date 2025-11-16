package tools;

import tools.Color;
import tools.Hit;
import tools.Ray;
import tools.Vec3;

public class Plane {
    private final Vec3 center;
    private final double radius;
    private final Color color;
    private final double yMin;

    public Plane(Vec3 center, double radius, Color color, double yMin) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.yMin = yMin;
    }

    //检测射线与弧形平面（球面上半部分）的相交
    public Hit hit(Ray ray){
        // 球面方程：(p - center)·(p - center) = radius²
        // 射线方程：p = ray.origin + t*ray.direction（t>0）
        Vec3 oc = ray.x().subtract(center);

        double a = ray.d().dot(ray.d());
        double b = 2 * oc.dot(ray.d());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null;
        }

        //两个解
        double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t = -1;

        //选择有效交点（t>0 且位于yMin上方，模拟地面部分）
        if (t1 > 0) {
            Vec3 p1 = ray.x().add(ray.d().multiply(t1));
            if (p1.y() >= yMin) {
                t = t1;
            }
        }
        if (t < 0 && t2 > 0) {
            Vec3 p2 = ray.x().add(ray.d().multiply(t2));
            if (p2.y() >= yMin) {
                t = t2;
            }
        }

        if (t < 0) {
            return null;
        }

        // 计算交点和法向量
        Vec3 position = ray.x().add(ray.d().multiply(t));  // 向量运算
        Vec3 normal = position.subtract(center).normalize();  //法向量计算（position - center）
        return new Hit(t, position, normal, color);
    }

    public Color color() {
        return color;
    }
}
