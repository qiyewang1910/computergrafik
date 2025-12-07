package tools;


public class Ebene implements Shape {
    public enum Ausdehnung { UNBEGRENZT, KREISRUND, QUADRATISCH }

    private final Ausdehnung typ;
    private final double parameter;
    private final Color color;
    private ImageTexture texture; // 新增：纹理属性（优先级高于纯色）
    private Mat44 transform; // 变换矩阵
    private Mat44 invTransform; // 逆变换矩阵
    private double textureScale = 0.1; // 新增：纹理缩放因子（控制平铺密度）

    // 无限平面
    public Ebene(Color color) {
        this(Ausdehnung.UNBEGRENZT, 0, color);
    }

    // 圆形平面
    public Ebene(double radius, Color color) {
        this(Ausdehnung.KREISRUND, radius, color);
    }

    // 正方形平面
    public Ebene(double seite, boolean istQuadrat, Color color) {
        this(Ausdehnung.QUADRATISCH, seite, color);
    }


    // ========== 新增：带纹理的构造方法（核心） ==========
    // 无限平面（纹理）
    public Ebene(ImageTexture texture) {
        this(Ausdehnung.UNBEGRENZT, 0, new Color(1,1,1,1)); // 纯色设为null，优先用纹理
        this.texture = texture;
    }
    // 带纹理+自定义缩放因子（可选）
    public Ebene(ImageTexture texture, double textureScale) {
        this(Ausdehnung.UNBEGRENZT, 0, new Color(1,1,1,1));
        this.texture = texture;
        this.textureScale = textureScale;
    }



    private Ebene(Ausdehnung typ, double parameter, Color color) {
        this.typ = typ;
        this.parameter = parameter;
        this.color = color;
        this.transform = Mat44.identity;
        this.invTransform = Mat44.identity;
    }

    public void setTransform(Mat44 transform) {
        this.transform = transform;
        this.invTransform = transform.invert();
    }
    public Mat44 getTransform() {
        return transform;
    }   
    public Mat44 getInvTransform() {
        return invTransform;
    }   

    // Y=0平面求交
    @Override
    public Hit intersect(Ray ray) {
        Ray transformedRay = ray.transform(invTransform);

        Vec3 rayDir = transformedRay.direction();
        Vec3 rayOrig = transformedRay.origin();

        // Y=0平面求交
        if (Math.abs(rayDir.y()) < 1e-9) { return null; }
        
        double t = -rayOrig.y() / rayDir.y();
        
        if (t < transformedRay.tmin() || t > transformedRay.tmax()) { return null; }

        Vec3 hitPos = transformedRay.at(t);

        // 6.检查交点是否在边界内
        boolean inBounds;
        switch (typ) {
            case UNBEGRENZT:
                inBounds = true;
                break;
            case KREISRUND:
                double distSq = hitPos.x() * hitPos.x() + hitPos.z() * hitPos.z();
                inBounds = distSq <= parameter * parameter + 1e-9;
                break;
            case QUADRATISCH:
                double halb = parameter / 2.0;
                inBounds = Math.abs(hitPos.x()) <= halb + 1e-9 && 
                          Math.abs(hitPos.z()) <= halb + 1e-9;
                break;
            default:
                inBounds = false;
        }
        
        if (!inBounds) { return null; }

        // 5. 计算局部坐标的法向量（局部坐标系中向上）
        Vec3 localNormal = new Vec3(0, 1, 0);
        
         // 射线起点的Y坐标 < 0 → 观察者在平面下方，法向量朝下（-Y）
        if (rayOrig.y() < 0) {
            localNormal = localNormal.multiply(-1);
        }

        // 将交点和法向量转换回世界坐标
        Vec3 worldHitPos = transform.multiplyPoint(hitPos); // 点变换
        Vec3 worldNormal = transform.multiplyDirection(localNormal).normalize(); // 法向量变换（仅旋转缩放）


        // 创建Hit对象（t值使用原始射线的参数，无需转换）
        return new Hit(t, worldHitPos, worldNormal, this, null);
       
    }


    // ========== 核心修改：支持纹理/纯色两种颜色获取方式 ==========
    // 原有getColor（兼容旧代码）
    @Override
    public Color getColor() {
        // 若有纹理，返回默认颜色（实际用getColorAt采样）；无纹理返回纯色
        return color;
    }


    // 新增：根据交点位置获取颜色（纹理/纯色）
    public Color getColorAt(Vec3 hitPoint) {
        // 1. 有纹理 → 采样纹理（无限平铺）
        if (texture != null) {
            // 将世界坐标的交点转换为局部坐标（消除平移/旋转影响）
            Vec3 localHitPos = invTransform.multiplyPoint(hitPoint);
            
            // 计算UV坐标：X/Z * 缩放因子，无限平铺（取小数部分）
            double u = localHitPos.x() * textureScale;
            double v = localHitPos.z() * textureScale;
            // 无限平铺 + 反转v轴（匹配图像坐标系）
            u = u - Math.floor(u);
            v = 1.0 - (v - Math.floor(v));
            
            // 采样纹理颜色
            return texture.getColor(new Vec2(u, v));
        }
        // 2. 无纹理 → 返回原有纯色
        return color;
    }

    // 设置纹理缩放因子（可选）
    public void setTextureScale(double scale) {
        this.textureScale = scale;
    }
}