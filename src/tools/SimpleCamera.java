package tools;

import tools.Ray;
import tools.Vec2;
import tools.Vec3;

public class SimpleCamera {
    private final double alpha;
    private final int width;
    private final int height;
    private final double d;

    public SimpleCamera(double alpha, int width, int height){
        this.alpha = alpha;
        this.width = width;
        this.height = height;
        this.d = (width / 2.0) / Math.tan(alpha/2.0);
    }

    public Ray generateRay(Vec2 pixel){
        //将像素坐标转换为图像平面的3D坐标
        double x3d = pixel.x() - width/2.0;
        double y3d = pixel.y() - height/2.0;
        double z3d = -d;

        Vec3 d = new Vec3(x3d, y3d, z3d).normalize();
        //射线原点在（0，0，0），有效范围t>0.001(避免与相机自身相交)
        return new Ray(new Vec3(0,0,0), d, 0.001, Double.POSITIVE_INFINITY);
    }
        
    public static void main(String[] args){
        SimpleCamera cam = new SimpleCamera(Math.PI/2,10,10);
        System.out.println(cam.generateRay(new Vec2(0,0)));
        System.out.println(cam.generateRay(new Vec2(5,5)));
        System.out.println(cam.generateRay(new Vec2(10,10)));
    }

    public Vec3 position() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'position'");
    }      
            
}
        
