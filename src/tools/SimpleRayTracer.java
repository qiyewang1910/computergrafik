package tools;

import java.util.List;
import tools.Color;
import tools.Hit;
import tools.Lichtquelle;
import tools.Plane;
import tools.Ray;
import tools.SimpleCamera;
import tools.Sphere;
import tools.Vec2;
import tools.Vec3;

import static tools.Functions.reflect;

public class SimpleRayTracer {
    private final SimpleCamera camera;
    private final List<Sphere> spheres;
    private final Color backgroundColor;
    private final List<Lichtquelle> lichtquelle;
    private final Plane ground;

   // 构造方法：参数顺序和初始化修正
    public SimpleRayTracer(
        SimpleCamera camera, 
        List<Sphere> spheres, 
        Plane groundPlane,  
        Color backgroundColor,        
        List<Lichtquelle> lichtquelle
    ) {
        this.camera = camera;
        this.spheres = spheres;
        this.ground = groundPlane;  
        this.backgroundColor = backgroundColor;
        this.lichtquelle = lichtquelle;
    }             
                        
                    
     /**
     * 为像素（x，y）计算颜色
     */
    public Color getColor(int x, int y){
        //1.生成从相机到像素（x,y）的射线
        Ray ray = camera.generateRay(new Vec2(x,y));
                    
        //2.查找射线与所有球体的最有效交点
        Hit closestHit = null;
        double minT = Double.POSITIVE_INFINITY;
            
        //检测与球体的相交
        for (Sphere sphere : spheres){
            Hit hit = sphere.intersect(ray);
            if (hit != null && hit.t() < minT && ray.isWithinBounds(hit.t())){
                minT = hit.t();
                closestHit = hit;
            }
        }    
        
        // 检测与地面的相交
        if (ground != null) {
            Hit groundHit = ground.hit(ray);  // 调用Plane的hit方法
            if (groundHit != null && groundHit.t() < minT && ray.isWithinBounds(groundHit.t())) {
                minT = groundHit.t();
                closestHit = groundHit;
            }
        }
                                       
        // 3. 根据交点生成颜色
        if (closestHit != null) {
            return shade(closestHit); // 使用光照模型计算颜色
        } else {
            return backgroundColor;  // 无交点，返回背景色
        }
    }                                        
                    
    /**
     * **最终修复的光照模型：强制高对比度和高光**
     */
    private Color shade(Hit hit){

        Vec3 p = hit.position();       // 交点坐标
        Vec3 n = hit.normal().normalize(); // 交点法向量（球体/地面的法向量）
        Vec3 v = camera.position().subtract(p).normalize(); // 视线方向
        Color objColor = hit.color();  // 物体颜色

        // 1.环境光: 0.05f 产生深阴影
        float umgebungsStaerke = 0.05f;
        Color umgebungslicht = hit.color().multiply(umgebungsStaerke);
                    
        // 漫反射 (产生明暗分割)
        Color diffuserTermTotal = Color.black();
        Color spiegelnderTermTotal = Color.black();
                    
        // 2. 遍历所有光源，计算每个光源的贡献
        for (Lichtquelle licht : lichtquelle) {
            // 关键：检测当前光源是否被遮挡（阴影）
            if (isInShadow(p, n, licht)) {
                continue; // 光源被遮挡，不贡献光照
            }
            // 光源方向（从交点到光源）和强度
            Vec3 l = licht.richtung(p).normalize(); // 光源方向l
            Color lightIntensity = licht.einfallend(p); // 光源强度（含衰减）

            // 3. 漫反射（兰伯特定律）
            double dotNL = Math.max(0, n.dot(l)); // n·l（确保非负）
            float diffuseStrength = 0.7f;
            Color diffuse = objColor
                .multiply(diffuseStrength * (float) dotNL) // k_d * (n·l)
                .multiplyWithColor(lightIntensity); // 乘以光源强度
            diffuserTermTotal = diffuserTermTotal.add(diffuse);

            // 4. 镜面反射（Phong模型）
            Vec3 r = reflect(l, n); // 反射方向r = 2*(n·l)*n - l（使用reflect工具函数）
            double dotRV = Math.max(0, r.dot(v)); // r·v（确保非负）
            float specularStrength = 0.5f;
            double shininess = 100; // 高光指数（值越大，高光越集中）
            Color specular = lightIntensity
                .multiply(specularStrength * (float) Math.pow(dotRV, shininess)); // k_s*(r·v)^n
            spiegelnderTermTotal = spiegelnderTermTotal.add(specular);
        }

    
        // 5. 最终颜色 = 环境光 + 漫反射 + 镜面反射
        return umgebungslicht.add(diffuserTermTotal).add(spiegelnderTermTotal);
    }    

    /**
     * 检测交点p是否在光源licht的阴影中
     * @param p 交点坐标
     * @param n 交点法向量（用于偏移射线起点）
     * @param licht 光源
     * @return true=在阴影中，false=不在阴影中
     */

     private boolean isInShadow(Vec3 p, Vec3 n, Lichtquelle licht) {
        // 1. 阴影射线起点：沿法向量偏移微小距离（避免自遮挡）
        double epsilon = 0.001; // 偏移量，防止射线与自身物体相交
        Vec3 shadowOrigin = p.add(n.multiply(epsilon));

        // 2. 阴影射线方向和最大距离（tMax）
        Vec3 shadowDir;
        double tMax;

        if (licht.isPunktlicht()) {
            // 点光源：射线方向指向光源位置，tMax为到光源的距离（减epsilon）
            Vec3 lightPos = licht.getPosition();
            Vec3 toLight = lightPos.subtract(p); // 从交点到光源的向量
            shadowDir = toLight.normalize();
            tMax = toLight.length() - epsilon; // 不超过光源位置
        } else {
            // 方向光源：射线方向与光源方向一致，tMax为无穷大
            shadowDir = licht.richtung(p).normalize(); // 从交点指向光源
            tMax = Double.POSITIVE_INFINITY;
        }

        // 3. 创建阴影射线（起点、方向、tMin=epsilon，tMax=计算值）
        Ray shadowRay = new Ray(shadowOrigin, shadowDir, epsilon, tMax);

        // 4. 检测阴影射线是否与球体相交
        for (Sphere sphere : spheres) {
            Hit hit = sphere.intersect(shadowRay);
            if (hit != null && shadowRay.isWithinBounds(hit.t())) { // 新增t范围判断
                return true;
            }
        }

        // 5. 检测阴影射线是否与地面相交
        if (ground != null) {
            Hit groundHit = ground.hit(shadowRay);
            if (groundHit != null && shadowRay.isWithinBounds(groundHit.t())) { // 新增t范围判断
                return true;
            }
        }

        // 6. 无遮挡，不在阴影中
        return false;
    }

}

