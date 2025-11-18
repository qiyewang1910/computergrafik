package tools;

import tools.Color;
import tools.Hit;
import tools.Ray;
import tools.Vec3;

public class Sphere implements Shape {
    private final Vec3 c;
    private final double r;
    private final Color color;

    public Sphere(Vec3 c, double r, Color color){
        this.c = c;
        this.r = r;
        this.color = color;
    }

    @Override
    public Hit intersect(Ray ray){
        Vec3 oc = ray.x().subtract(c);
        double a = ray.d().dot(ray.d());
        double b = 2*oc.dot(ray.d());
        double c_quad = oc.dot(oc) - r*r;
        double discriminant = b * b - 4 * a * c_quad;

        if (discriminant < 0){
            return null;
        }

        double sqrtDisc = Math.sqrt(discriminant);
        double t0 = (-b - sqrtDisc) / (2 * a);
        double t1 = (-b + sqrtDisc) / (2 * a);

        double t = t0;
        if (!ray.isWithinBounds(t)) {
            t = t1;
            if (!ray.isWithinBounds(t)) {
                return null;
            }
        }

        // 计算交点位置和法向量
        Vec3 point = ray.at(t);
        Vec3 normal = point.subtract(c).normalize();
        return new Hit(t, point, normal, color);
    }

     // 新增：获取球体颜色（供光照计算使用）
    public Color getColor() {
        return this.color;
    }

}
