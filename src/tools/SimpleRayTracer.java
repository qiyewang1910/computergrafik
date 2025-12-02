package tools;

import java.util.List;
import tools.Color;
import tools.Hit;
import tools.Lichtquelle;
import tools.Ray;
import tools.Shape;
import tools.SimpleCamera;
import tools.Vec2;
import tools.Vec3;

import static tools.Functions.reflect;

public class SimpleRayTracer {
    private final SimpleCamera camera;
    private final List<Shape> scene;
    private final Color backgroundColor;
    private final List<Lichtquelle> lichtquelle;


    // 构造方法：初始化场景组件
    public SimpleRayTracer(
        SimpleCamera camera, 
        List<Shape> scene, 
        Color backgroundColor,        
        List<Lichtquelle> lichtquelle
    ) {
        this.camera = camera;
        this.scene = scene;
        this.backgroundColor = backgroundColor;
        this.lichtquelle = lichtquelle;
    }             


    public Color getColor(int x, int y) {
        // 1. 生成从相机到像素的射线
        Ray ray = camera.generateRay(new Vec2(x, y));
        return trace(ray, 5);
    }
    
    public Color trace(Ray ray, int depth) {
        if (depth <= 0) {
            return backgroundColor; // 达到递归深度，返回黑色
        }

        // 查找最近交点
        Hit closestHit = findClosestHit(ray);

        if (closestHit == null) {
            return backgroundColor; // 无交点返回背景色
        }

        // 计算当前表面的光照颜色
        Color localColor = shade(closestHit);
        
        // 获取物体原始颜色的透明度
        Color rawColor = getShapeColor(closestHit.shape());
        double alpha = rawColor.a(); // 使用你新加的 alpha()

        // 如果物体是半透明 (alpha < 1.0)，则继续追踪
        if (alpha < 1.0 - 1e-4) { // 稍微容错
            // 射线的起点稍微往前移一点点，防止打到自己
            Vec3 offsetOrigin = closestHit.position().add(ray.direction().multiply(0.001));
            Ray nextRay = new Ray(offsetOrigin, ray.direction(), 0, Double.POSITIVE_INFINITY);
            
            // 递归获取背景颜色
            Color colorBehind = trace(nextRay, depth - 1);
            
            // 混合公式：(表面色 * alpha) + (背景色 * (1-alpha))
            Color blended = localColor.multiply(alpha)
                                      .add(colorBehind.multiply(1.0 - alpha));
            return blended.clamp();
        }

        return localColor.clamp();
    }


    /**
     * 查找射线与场景中所有形状的最近交点
     */
    private Hit findClosestHit(Ray ray) {
        Hit closestHit = null;
        double minT = Double.POSITIVE_INFINITY;

        for (Shape shape : scene) {
            if (shape == null) continue;
            
            Hit hit = shape.intersect(ray);
            if (hit != null) {
                double t = hit.t();
                if (t < minT && ray.isWithinBounds(t)) {
                    minT = t;
                    closestHit = hit;
                }
            }
        }
        return closestHit;
    }

    /**
     * 工具方法：从Shape中获取颜色
     */
    private Color getShapeColor(Shape shape) {
        if (shape == null) {
            return Color.black(); // 空值默认黑色
        }
        // 适配Sphere的getColor()方法
        if (shape instanceof Sphere) {
            return ((Sphere) shape).getColor();
        }
        // 适配Plane的getColor()方法（若有Plane类）
        else if (shape instanceof Ebene) {
            return ((Ebene) shape).getColor();
        }
        // Group默认灰色
        else if (shape instanceof Group) {
            // Group 本身通常没有颜色，Hit 返回的是子物体

             return Color.white;
        }
        // 未知形状默认白色
        else {
            return new Color(1.0, 1.0, 1.0, 1.0);
        }
    }



    /**
     * 光照计算：环境光 + 漫反射 + 镜面反射 + 阴影
     */
    private Color shade(Hit hit) {
        Vec3 p = hit.position();       // 交点坐标
        Vec3 n = hit.normal().normalize();  // 法向量归一
        Color objColor = getShapeColor(hit.shape());

        // 环境光
        float ambientStrength = 0.1f;
        Color ambient = objColor.multiply(ambientStrength);

        // 漫反射 + 镜面反射
        Color diffuseTotal = Color.black();
        Color specularTotal = Color.black();

        // 如果没有光源，只返回环境光
        if (lichtquelle == null || lichtquelle.isEmpty()) {
            return ambient;
        }

        // 遍历所有光源
        for (Lichtquelle licht : lichtquelle) {
            if (licht == null) continue;

            // 检测阴影：被遮挡则跳过该光源
            if (isInShadow(p, n, licht)) {
                continue;
            }

            // 光源方向（从交点到光源）和强度
            Vec3 l = licht.richtung(p).normalize();
            Color lightIntensity = licht.einfallend(p);

            // 3. 漫反射（兰伯特定律）
            double dotPktDiffus = Math.max(0, n.dot(l));  // 避免背面受光
            float diffuseStrength = 0.7f;
            Color diffuse = objColor
                .multiply(diffuseStrength * dotPktDiffus)
                .multiplyWithColor(lightIntensity);
            diffuseTotal = diffuseTotal.add(diffuse);

            
            // 4. Spiegelnder Term
           // if (!licht.isPunktlicht()) {
            Vec3 einfallsRichtung = l.multiply(-1).normalize();  // 入射方向
            Vec3 r = reflect(n, einfallsRichtung).normalize(); // 反射方向
            Vec3 blickRichtung = camera.position().subtract(p).normalize(); // 视线方向

            // Color ankommendeIntensitaet = licht.einfallend(p);   //入射光强
            Color spiegelnderReflexionskoeffizient = new Color(1.0,1.0,1.0,1);    //镜面反射系数
            float spiegelungsStaerke = 0.4f;  // 镜面反射强度
            double glanzExponent = 30;    //高光指数（越大越集中）
                

            double dotPkt = Math.max(0, r.dot(blickRichtung));  // 反射方向与视线夹角
            Color spiegelnderTerm = lightIntensity
                .multiply(spiegelungsStaerke)
                .multiply((float) Math.pow(dotPkt, glanzExponent))
                .multiplyWithColor(spiegelnderReflexionskoeffizient);
            specularTotal = specularTotal.add(spiegelnderTerm);    
            //}
        }

        // 5. 最终颜色合成
        return ambient.add(diffuseTotal).add(specularTotal);
    }

    /**
     * 检测交点是否在光源的阴影中
     */
    private boolean isInShadow(Vec3 p, Vec3 n, Lichtquelle licht) {
        // 1. 阴影射线起点：沿法向量偏移（避免自遮挡）
        double epsilon = 0.001;
        Vec3 shadowOrigin = p.add(n.multiply(epsilon));

        // 2. 计算阴影射线方向和最大距离
        Vec3 shadowDir;
        double tMax;

        if (licht.isPunktlicht()) {
            // 点光源：射线指向光源位置
            Vec3 lightPos = licht.getPosition();
            if (lightPos == null) return false;

            Vec3 toLight = lightPos.subtract(p);
            shadowDir = toLight.normalize();
            tMax = toLight.length() - epsilon;  // 不超过光源位置
        } else {
            // 方向光源：射线方向与光源方向一致
            shadowDir = licht.richtung(p).normalize();
            tMax = Double.POSITIVE_INFINITY;
        }

        // 3. 创建阴影射线
        Ray shadowRay = new Ray(shadowOrigin, shadowDir, epsilon, tMax);

        // 4. 检测与球体的遮挡
        for (Shape shape : scene) {
            Hit hit = shape.intersect(shadowRay);
            if (hit != null && shadowRay.isWithinBounds(hit.t())) {
                return true; // 被任何形状遮挡改成了false
            }
        }

        // 6. 无遮挡
        return false;
    }
}