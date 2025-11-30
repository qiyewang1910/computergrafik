package tools;

import java.util.ArrayList;
import java.util.List;

public class Group implements Shape { // 必须实现Shape接口
    private List<Shape> children = new ArrayList<>();
    private Mat44 transform = new Mat44(); // 组合变换矩阵（默认单位矩阵）
    private Mat44 invTransform; // 逆矩阵（用于射线变换）

    // 新增：设置变换矩阵，并预计算逆矩阵
    public void setTransform(Mat44 transform) {
        this.transform = transform;
        this.invTransform = transform.invert(); // 依赖之前补充的invert()方法
    }

    // 新增：获取变换矩阵
    public Mat44 getTransform() {
        return transform;
    }

    // 新增：获取逆变换矩阵
    public Mat44 getInvTransform() {
        return invTransform;
    }

    // 添加子形状（参数为Shape，兼容所有实现类）
    public void addChild(Shape child) { // 关键：参数是Shape，不是Sphere
        children.add(child);
    }

    // 实现intersect方法（遍历子形状检测交点）
    @Override
    public Hit intersect(Ray ray) {
        // 1. 空值保护：射线为null直接返回
        if (ray == null) return null;
        
        // 2. 获取逆矩阵（确保非null）
        Mat44 invMat = getInvTransform();
        
        // 3. 将世界坐标系射线转换为Group局部坐标系
        Ray localRay = ray.transform(invMat);

        // 4. 遍历所有子形状求交
        Hit closestHit = null;
        double minT = Double.POSITIVE_INFINITY;
        for (Shape child : children) {
            if (child == null) continue; // 子形状空值保护
            Hit hit = child.intersect(localRay);
            if (hit != null) {
                // 5. 将局部坐标系交点转换回世界坐标系
                Hit worldHit = hit.transform(this.transform);
                double t = worldHit.t();
                if (t < minT && ray.isWithinBounds(t)) {
                    minT = t;
                    closestHit = worldHit;
                }
            }
        }
        return closestHit;
    }
}