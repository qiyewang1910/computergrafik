package tools;

import tools.Ray;
import tools.Vec2;
import tools.Vec3;

public class SimpleCamera {
    private final double alpha;  //视场角
    private final int width;    //图像宽度
    private final int height;
    private final double d;     //成像平面距离
    private final Vec3 position; // 相机位置（新增成员变量）

    public SimpleCamera(double alpha, int width, int height, Vec3 position){
        this.alpha = alpha;
        this.width = width;
        this.height = height;
        this.d = (width / 2.0) / Math.tan(alpha/2.0);
        this.position = position;  // 初始化相机位置
    }

    // 生成射线时，原点使用相机位置，方向指向场景
    public Ray generateRay(Vec2 pixel){
        //将像素坐标转换为图像平面的3D坐标
        double x3d = pixel.x() - width/2.0;
        double y3d = pixel.y() - height/2.0;
        double z3d = -d;

        Vec3 d = new Vec3(x3d, y3d, z3d).normalize();
        //射线原点在（0，0，0），有效范围t>0.001(避免与相机自身相交)
        return new Ray(new Vec3(1,0,0), d, 0.001, Double.POSITIVE_INFINITY);
    }

    public Vec3 position() {
        return position;  // 直接返回相机位置
    }      
            
}
        
