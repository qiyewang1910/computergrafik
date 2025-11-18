package tools;

import tools.Color;
import tools.Hit;
import tools.Ray;
import tools.Shape;
import tools.Vec3;

public class Plane implements Shape {
    private final Vec3 center;    // 球面中心
    private final double radius;  // 球面半径
    private final Color color;    // 地面颜色
    private final double yMin;    // 保留y >= yMin的部分

    public Plane(Vec3 center, double radius, Color color, double yMin) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.yMin = yMin;
    }

    // 新增：方法名从hit改为intersect，添加@Override
    @Override
    // 检测射线与弧形平面（球面上半部分）的相交
    public Hit intersect (Ray ray) {
        // 球面方程：(p - center)·(p - center) = radius²
        Vec3 oc = ray.x().subtract(center); // 射线原点到球心的向量（ray.x()是原点）

        // 解二次方程：a*t² + b*t + c = 0
        double a = ray.d().dot(ray.d()); // 射线方向向量点积（ray.d()是方向）
        double b = 2 * oc.dot(ray.d());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null; // 无交点
        }

        // 计算两个解
        double sqrtDisc = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDisc) / (2 * a);
        double t2 = (-b + sqrtDisc) / (2 * a);
        double t = -1;

        // 选择有效交点（t>0 且位于yMin上方，模拟地面部分）
        if (t1 > 0) {
            Vec3 p1 = ray.x().add(ray.d().multiply(t1)); // 计算t1对应的点
            if (p1.y() <= yMin) {
                t = t1;
            }
        }
        if (t < 0 && t2 > 0) { // 若t1无效，检查t2
            Vec3 p2 = ray.x().add(ray.d().multiply(t2)); // 计算t2对应的点
            if (p2.y() >= yMin) {
                t = t2;
            }
        }

        if (t < 0) {
            return null; // 无有效交点
        }

        // 计算交点和法向量（法向量从球心指向交点，确保向上）
        Vec3 position = ray.x().add(ray.d().multiply(t));
        Vec3 normal = position.subtract(center).normalize();
        // 修正法向量方向：确保向上（避免阴影检测错误）
        if (normal.y() < 0) {
            normal = normal.multiply(-1);
        }

        return new Hit(t, position, normal, color);
    }

    // 保留原有的color()方法（不修改命名）
    public Color color() {
        return color;
    }
}