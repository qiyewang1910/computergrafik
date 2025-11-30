package tools;

import static tools.Functions.transpose;

public class Hit {
    private final double t;  // 光线传播距离
    private final Vec3 p;    // 相交点坐标
    private final Vec3 normal;    //相交点法向量（单位向量，物体外部）
    private final Shape shape; //相交的形状


    public Hit(double t, Vec3 p, Vec3 normal, Shape shape) {
        this.t = t;
        this.p = p;
        this.normal = normal;
        this.shape = shape;
    }

  
    // ========== 新增核心：矩阵变换（局部坐标系→世界坐标系） ==========
    /**
     * 用4x4矩阵变换交点（核心坐标系转换方法）
     * @param mat 变换矩阵（Group的局部→世界矩阵）
     * @return 变换后的新交点
     */
    public Hit transform(Mat44 mat) {
        // 1. 变换交点位置（点变换：w=1）
        Vec3 newPosition = transformPoint(mat, this.p);
        
        // 2. 变换法向量（特殊处理：用矩阵的逆转置矩阵，保证垂直性）
        Vec3 newNormal = transformNormal(mat, this.normal);
        
        // 3. 生成新交点（t值不变，形状不变）
        return new Hit(this.t, newPosition, newNormal, this.shape);
    }

    // ========== 工具方法：点/法向量的矩阵变换 ==========
    /**
     * 变换点（和Ray中逻辑一致）
     */
    private Vec3 transformPoint(Mat44 mat, Vec3 point) {
        double x = mat.get(0, 0) * point.x() + mat.get(1, 0) * point.y() + mat.get(2, 0) * point.z() + mat.get(3, 0) * 1.0;
        double y = mat.get(0, 1) * point.x() + mat.get(1, 1) * point.y() + mat.get(2, 1) * point.z() + mat.get(3, 1) * 1.0;
        double z = mat.get(0, 2) * point.x() + mat.get(1, 2) * point.y() + mat.get(2, 2) * point.z() + mat.get(3, 2) * 1.0;
        return new Vec3(x, y, z);
    }

    /**
     * 变换法向量（核心：用矩阵的逆转置矩阵，避免缩放导致法向量偏离）
     */
    private Vec3 transformNormal(Mat44 mat, Vec3 normal) {
        // 步骤1：计算原矩阵的逆矩阵（适配你的Mat44，需先补充invert方法）
        Mat44 invMat = mat.invert();
        // 步骤2：计算逆矩阵的转置（逆转置矩阵）
        Mat44 invTransMat = transpose(invMat);
        
        // 步骤3：用法向量乘以逆转置矩阵（向量变换，w=0）
        double x = invTransMat.get(0, 0) * normal.x() + invTransMat.get(1, 0) * normal.y() + invTransMat.get(2, 0) * normal.z();
        double y = invTransMat.get(0, 1) * normal.x() + invTransMat.get(1, 1) * normal.y() + invTransMat.get(2, 1) * normal.z();
        double z = invTransMat.get(0, 2) * normal.x() + invTransMat.get(1, 2) * normal.y() + invTransMat.get(2, 2) * normal.z();
        
        return new Vec3(x, y, z).normalize(); // 法向量重新归一化
    }

    

    public double t() { 
        return t; 
    }

    public Vec3 p() { 
        return p; 
    }
    public Vec3 normal(){ 
        return normal; 
    }

    public Shape shape() {
        return shape;
    }

    public Vec3 position(){
        return p;
    }

}
