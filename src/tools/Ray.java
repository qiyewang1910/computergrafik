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


    /**
     * 矩阵变换（世界→局部 / 局部→世界）
     * 用4x4矩阵变换射线（核心坐标系转换方法）
     * @param mat 变换矩阵（Mat44）
     * @return 变换后的新射线
     */
    public Ray transform(Mat44 mat) {
        if (mat == null) return this; // 矩阵为null返回原射线
        Vec3 newOrigin = transformPoint(mat, this.x);    // 1. 变换射线原点（点变换：齐次坐标w=1，受平移影响）
        Vec3 newDirection = transformVector(mat, this.d);     // 2. 变换射线方向（向量变换：齐次坐标w=0，不受平移影响）
        
        return new Ray(newOrigin, newDirection, this.tmin, this.tmax);   // 3. 生成新射线（tMin/tMax不变，方向重新归一化）
    }

    /**
     * 工具方法：点/向量的矩阵变换（适配Mat44列主序）
     * 变换点（齐次坐标w=1）
     */
    private Vec3 transformPoint(Mat44 mat, Vec3 point) {
        if (mat == null || point == null) return new Vec3(0,0,0); // 空值保护
        double x = mat.get(0, 0) * point.x() + mat.get(1, 0) * point.y() + mat.get(2, 0) * point.z() + mat.get(3, 0) * 1.0;
        double y = mat.get(0, 1) * point.x() + mat.get(1, 1) * point.y() + mat.get(2, 1) * point.z() + mat.get(3, 1) * 1.0;
        double z = mat.get(0, 2) * point.x() + mat.get(1, 2) * point.y() + mat.get(2, 2) * point.z() + mat.get(3, 2) * 1.0;
        return new Vec3(x, y, z);
    }

    /**
     * 变换向量（齐次坐标w=0，不平移）
     */
    private Vec3 transformVector(Mat44 mat, Vec3 vector) {
        double x = mat.get(0, 0) * vector.x() + mat.get(1, 0) * vector.y() + mat.get(2, 0) * vector.z() + mat.get(3, 0) * 0.0;
        double y = mat.get(0, 1) * vector.x() + mat.get(1, 1) * vector.y() + mat.get(2, 1) * vector.z() + mat.get(3, 1) * 0.0;
        double z = mat.get(0, 2) * vector.x() + mat.get(1, 2) * vector.y() + mat.get(2, 2) * vector.z() + mat.get(3, 2) * 0.0;
        return new Vec3(x, y, z).normalize(); // 向量变换后重新归一化
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