package tools;

import tools.Color;
import tools.Hit;
import tools.Mat44;
import tools.Ray;
import tools.Shape;
import tools.Vec3;

public class Ebene implements Shape {
    public enum Ausdehnung { UNBEGRENZT, KREISRUND, QUADRATISCH }

    private final Ausdehnung typ;
    private final double parameter;
    private final Color color;
    private Mat44 transform; // 变换矩阵
    private Mat44 invTransform; // 逆变换矩阵

    // 构造方法：无限平面
    public Ebene(Color color) {
        this(Ausdehnung.UNBEGRENZT, 0, color);
    }

    // 构造方法：圆形平面
    public Ebene(double radius, Color color) {
        this(Ausdehnung.KREISRUND, radius, color);
    }

    // 构造方法：正方形平面
    public Ebene(double seite, boolean istQuadrat, Color color) {
        this(Ausdehnung.QUADRATISCH, seite, color);
    }

    private Ebene(Ausdehnung typ, double parameter, Color color) {
        this.typ = typ;
        this.parameter = parameter;
        this.color = color;
        this.transform = Mat44.identity;
        this.invTransform = Mat44.identity;
    }

    public void setTransform(Mat44 transform) {
        this.transform = transform;
        this.invTransform = transform.invert();
    }
    public Mat44 getTransform() {
        return transform;
    }   
    public Mat44 getInvTransform() {
        return invTransform;
    }   

    // Y=0平面求交
    @Override
    public Hit intersect(Ray ray) {
        ray.transform(invTransform);

        Vec3 rayDir = ray.direction();
        Vec3 rayOrig = ray.origin();

        // Y=0平面求交
        if (Math.abs(rayDir.y()) < 1e-9) return null;
        
        double t = -rayOrig.y() / rayDir.y();
        
        if (t < ray.tmin() || t > ray.tmax()) return null;

        Vec3 hitPos = ray.at(t);

        // 范围判断（仅X/Z轴）
        boolean inBounds;
        switch (typ) {
            case UNBEGRENZT:
                inBounds = true;
                break;
            case KREISRUND:
                double distSq = hitPos.x() * hitPos.x() + hitPos.z() * hitPos.z();
                inBounds = distSq <= parameter * parameter + 1e-9;
                break;
            case QUADRATISCH:
                double halb = parameter / 2.0;
                inBounds = Math.abs(hitPos.x()) <= halb + 1e-9 && 
                          Math.abs(hitPos.z()) <= halb + 1e-9;
                break;
            default:
                inBounds = false;
        }
        
        if (!inBounds) return null;

        // 法向量（局部坐标系中向上）
        Vec3 localNormal = new Vec3(0, 1, 0);
        if (rayDir.y() > 0) {
            localNormal = localNormal.multiply(-1);
        }

        // 创建局部坐标系的Hit，然后变换到世界坐标系
        Hit localHit = new Hit(t, hitPos, localNormal, this);
        return localHit.transform(transform);
    }

    // 获取平面颜色
    @Override
    public Color getColor() {
        return color;
    }
}