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

    // 构造方法：初始化相机参数，包括位置
    public SimpleCamera(double alpha, int width, int height, Vec3 position) {
        this.alpha = alpha;
        this.width = width;
        this.height = height;
        this.d = (width / 2.0) / Math.tan(alpha / 2.0); // 计算成像平面距离
        this.position = position; // 初始化相机位置（关键：保存传入的位置）
    }

    // 生成从相机到像素的射线
    public Ray generateRay(Vec2 pixel) {
        // 将像素坐标转换为成像平面的3D坐标（原点在图像中心）
        double x3d = pixel.x() - width / 2.0;   // X偏移（左负右正）
        double y3d = -(pixel.y() - height / 2.0); // Y偏移（上正下负，注意负号）
        double z3d = -d;                        // Z轴指向场景（负方向）

        // 射线方向：从相机位置指向成像平面上的点（归一化）
        Vec3 direction = new Vec3(x3d, y3d, z3d).normalize();

        // 射线原点为相机位置（修正：使用this.position，而非cameraPosition）
        return new Ray(position, direction, 0.001, Double.POSITIVE_INFINITY);
    }

    // 获取相机位置（供SimpleRayTracer使用）
    public Vec3 position() {
        return position;
    }
}