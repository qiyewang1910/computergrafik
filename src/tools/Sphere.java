package tools;


public class Sphere implements Shape {
    private final Vec3 c;
    private final double r;
    private final Color color;
    private final ImageTexture texture;

    // 纯色构造函数
    public Sphere(Vec3 c, double r, Color color,Material material){
        this.c = c;
        this.r = r;
        this.color = color;
        this.texture = null;
    }

    // 纹理构造函数
    public Sphere(Vec3 c, double r, ImageTexture texture) {
        this.c = c;
        this.r = r;
        this.color = null;
        this.texture = texture;
    }


    @Override
    public Hit intersect(Ray ray){
        Vec3 oc = ray.x().subtract(c);
        double a = ray.d().dot(ray.d());
        double b = 2*oc.dot(ray.d());
        double c_quad = oc.dot(oc) - r*r;
        double discriminant = b * b - 4 * a * c_quad;

        if (discriminant < 0){
            return null;
        }

        double sqrtDisc = Math.sqrt(discriminant);
        double t0 = (-b - sqrtDisc) / (2 * a);
        double t1 = (-b + sqrtDisc) / (2 * a);

        double t = t0;
        if (!ray.isWithinBounds(t)) {
            t = t1;
            if (!ray.isWithinBounds(t)) {
                return null;
            }
        }

        // 计算交点位置和法向量
        Vec3 point = ray.at(t);
        Vec3 normal = point.subtract(c).normalize();

        // 计算UV坐标（球面转纹理坐标）
        double u = 0.5 + Math.atan2(normal.z(), normal.x()) / (2 * Math.PI);
        double v = 0.5 - Math.asin(normal.y()) / Math.PI;
        Vec2 uv = new Vec2(u, v);
        
        return new Hit(t, point, normal, this, uv);
    }

    @Override
    public Color getColor() {
        // 如果有纹理，返回白色（实际颜色通过getColorAt获取）
        if (texture != null) {
            return new Color(1, 1, 1, 1);
        }
        return this.color;
    }

    // 根据UV坐标获取颜色
    public Color getColorAt(Vec2 uv) {
        if (texture != null) {
            return texture.sample(uv);
        }
        return color;
    }

}
