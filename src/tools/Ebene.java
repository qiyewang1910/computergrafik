package tools;

import tools.Color;
import tools.Hit;
import tools.Ray;
import tools.Shape;
import tools.Vec3;

public class Ebene implements Shape {
    public enum Ausdehnung { UNBEGRENZT, KREISRUND, QUADRATISCH }

    private final Ausdehnung typ;
    private final double parameter;
    private final Color color;

    // 构造方法
    public Ebene(Color color) {
        this(Ausdehnung.UNBEGRENZT, 0, color);
    }

    public Ebene(double radius, Color color) {
        this(Ausdehnung.KREISRUND, radius, color);
    }

    public Ebene(double seite, boolean istQuadrat, Color color) {
        this(Ausdehnung.QUADRATISCH, seite, color);
    }

    private Ebene(Ausdehnung typ, double parameter, Color color) {
        this.typ = typ;
        this.parameter = parameter;
        this.color = color;
    }

    // 仅支持Y=0平面求交（无叉乘）
    @Override
    public Hit intersect(Ray ray) {
        Vec3 rayDir = ray.direction();
        Vec3 rayOrig = ray.origin();

        // Y=0平面求交
        if (Math.abs(rayDir.y()) < 1e-9) return null;
        double t = -rayOrig.y() / rayDir.y();
        if (t < ray.tmin() || t > ray.tmax()) return null;

        Vec3 hitPos = ray.at(t);

        // 范围判断（仅X/Z轴）
        boolean inBounds = switch (typ) {
            case UNBEGRENZT -> true;
            case KREISRUND -> {
                double distSq = hitPos.x() * hitPos.x() + hitPos.z() * hitPos.z();
                yield distSq <= parameter * parameter + 1e-9;
            }
            case QUADRATISCH -> {
                double halb = parameter / 2.0;
                yield Math.abs(hitPos.x()) <= halb + 1e-9 && 
                      Math.abs(hitPos.z()) <= halb + 1e-9;
            }
        };
        if (!inBounds) return null;

        // 法向量
        Vec3 normal = new Vec3(0, 1, 0);
        if (rayDir.y() > 0) normal = normal.multiply(-1);

        return new Hit(t, hitPos, normal, this);
    }

    public Color getColor() {
        return color;
    }

}