package tools;

public class Hit {
    private final double t;  // 光线传播距离
    private final Vec3 p;    // 相交点坐标
    private final Vec3 normal;    //相交点法向量（单位向量，物体外部）
    private final Color color;   //表面颜色


    public Hit(double t, Vec3 p, Vec3 normal, Color color){
        this.t = t;
        this.p = p;
        this.normal = normal;
        this.color = color;
    }

    //getter
    public double t() { 
        return t; 
    }

    public Vec3 p() { 
        return p; 
    }
    public Vec3 normal(){ 
        return normal; 
    }

    public Color color() {
        return color;
    }

    public Vec3 position() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'position'");
    }  
}
