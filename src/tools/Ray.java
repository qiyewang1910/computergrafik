package tools;

public class Ray {
    private final Vec3 x;       // 射线原点
    private final Vec3 d;       // 射线方向
    private final double tmin;  // 最小t值
    private final double tmax;  // 最大t值

    public Ray(Vec3 x, Vec3 d, double tmin, double tmax) {
        this.x = x;
        this.d = d.normalize();  // 确保方向向量归一化
        this.tmin = tmin;
        this.tmax = tmax;
    }

    // 计算射线在t处的点（x + t*d）
    public Vec3 at(double t) {
        return x.add(d.multiply(t));
    }

    // 判断t是否在[tmin, tmax]范围内
    public boolean isWithinBounds(double t) {
        return t >= tmin && t <= tmax;
    }

    // Getter
    public Vec3 x() {
        return x;  // 射线原点（与你的调用方式匹配）
    }

    public Vec3 d() {
        return d;  // 射线方向（与你的调用方式匹配）
    }

    public double tmin() {
        return tmin;
    }

    public double tmax() {
        return tmax;
    }

    // 修正：统一返回Vec3类型，适配外部代码中可能的origin()/direction()调用
    public Vec3 origin() {
        return x;  // 与x()保持一致，返回原点
    }

    public Vec3 direction() {
        return d;  // 与d()保持一致，返回方向（修正原String返回类型错误）
    }
}