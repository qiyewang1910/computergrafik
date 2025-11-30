package tools;

import tools.Ray;
import tools.Vec2;
import tools.Vec3;

public class SimpleCamera {
    private final double alpha;  // 视场角
    private final int width;     // 图像宽度
    private final int height;    // 图像高度
    private final double d;      // 成像平面距离
    private final Vec3 position; // 相机位置（成员变量）
    private final Vec3 forward;
    private final Vec3 right;
    private final Vec3 up;
    

    // 初始化相机参数，包括位置
    public SimpleCamera(double alpha, int width, int height, Vec3 position) {
        this.alpha = alpha;
        this.width = width;
        this.height = height;
        this.d = (width / 2.0) / Math.tan(alpha / 2.0); // 计算成像平面距离
        this.position = position; // 初始化相机位置（关键：保存传入的位置）

        // 默认朝向：沿-Z轴（和原有逻辑一致）
        this.forward = new Vec3(0, 0, -1).normalize();
        this.right = new Vec3(1, 0, 0).normalize(); // 右向为+X
        this.up = new Vec3(0, 1, 0).normalize();    // 上向为+Y
    }

    // 构造方法2: 支持自定义朝向（平移+旋转）
    public SimpleCamera(double alpha, int width, int height, Vec3 position, Vec3 forwardDir) {
        this.alpha = alpha;
        this.width = width;
        this.height = height;
        this.d = (width / 2.0) / Math.tan(alpha / 2.0);
        this.position = position;
        
        // 直接使用传入的前向向量（已归一化）
        this.forward = forwardDir.normalize();
        // 简化：右向=前向的XZ平面垂直方向，上向固定为+Y（足够满足基础旋转需求）
        this.right = new Vec3(-this.forward.z(), 0, this.forward.x()).normalize();
        this.up = new Vec3(0, 1, 0).normalize();
    }


    // 生成从相机到像素的射线
    public Ray generateRay(Vec2 pixel) {
        // 将像素坐标转换为成像平面的3D坐标（原点在图像中心）
        double x3d = pixel.x() - width / 2.0;   // X偏移（左负右正）
        double y3d = -(pixel.y() - height / 2.0); // Y偏移（上正下负，注意负号）
    

        // 2. 核心：将成像平面偏移转换为世界坐标系的方向（支持旋转）
        Vec3 dir = right.multiply(x3d)       // 右向偏移
                        .add(up.multiply(y3d)) // 上向偏移
                        .add(forward.multiply(-d)) // 前向（朝向）偏移
                        .normalize(); // 归一化
        

        // 射线原点为相机位置（修正：使用this.position，而非cameraPosition）
        return new Ray(position, dir, 0.001, Double.POSITIVE_INFINITY);
    }

    // 获取相机位置（供SimpleRayTracer使用）
    public Vec3 position() {
        return position;
    }
}