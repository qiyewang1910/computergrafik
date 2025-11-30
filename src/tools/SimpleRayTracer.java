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

    /**
     * 计算像素(x, y)的颜色
     */
    public Color getColor(int x, int y) {
        // 1. 生成从相机到像素的射线
        Ray ray = camera.generateRay(new Vec2(x, y));

        // 2. 查找射线与所有物体的最近交点
        Hit closestHit = null;
        double minT = Double.POSITIVE_INFINITY;

        // 遍历scene中所有形状（包括球体、地面、Group）
        for (Shape shape : scene) {
            Hit hit = shape.intersect(ray); // 调用每个Shape的intersect方法
            if (hit != null) {
                double t = hit.t();
                if (t < minT && ray.isWithinBounds(t)) {
                    minT = t;
                    closestHit = hit;
                }
            }
        }

    

        // 3. 计算交点颜色或返回背景色
        if (closestHit != null) {
            Color shadedColor = shade(closestHit);
            // 若Color类有clamp方法则调用，否则直接返回（兼容无clamp的情况）
            try {
                return shadedColor.clamp();
            } catch (NoSuchMethodError e) {
                return shadedColor;
            }
        } else {
            return backgroundColor;
        }
    }

    

    /**
     * 光照计算：环境光 + 漫反射 + 镜面反射 + 阴影
     */
    private Color shade(Hit hit) {
        Vec3 p = hit.position();       // 交点坐标
        Vec3 n = hit.normal().normalize();  // 法向量归一
        Color objColor = hit.color();  // 物体颜色

        // 环境光
        float ambientStrength = 0.08f;
        Color ambient = objColor.multiply(ambientStrength);

        // 漫反射 + 镜面反射
        Color diffuseTotal = Color.black();
        Color specularTotal = Color.black();

        // 遍历所有光源
        for (Lichtquelle licht : lichtquelle) {
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
                .multiply(diffuseStrength * (float) dotPktDiffus)
                .multiplyWithColor(lightIntensity);
            diffuseTotal = diffuseTotal.add(diffuse);

            
            // 4. Spiegelnder Term
            if (!licht.isPunktlicht()) {
            Vec3 einfallsRichtung = l.multiply(-1).normalize();  // 入射方向
            Vec3 r = reflect(einfallsRichtung, n).normalize(); // 反射方向
            Vec3 blickRichtung = camera.position().subtract(p).normalize(); // 视线方向

            // Color ankommendeIntensitaet = licht.einfallend(p);   //入射光强
            Color spiegelnderReflexionskoeffizient = new Color(1.0,1.0,1.0);    //镜面反射系数
            float spiegelungsStaerke = 0.8f;  // 镜面反射强度
            double glanzExponent = 30;    //高光指数（越大越集中）
                

            double dotPkt = Math.max(0, r.dot(blickRichtung));  // 反射方向与视线夹角
            Color spiegelnderTerm = lightIntensity
                .multiply(spiegelungsStaerke)
                .multiply((float) Math.pow(dotPkt, glanzExponent))
                .multiplyWithColor(spiegelnderReflexionskoeffizient);
            specularTotal = specularTotal.add(spiegelnderTerm);    
            }
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