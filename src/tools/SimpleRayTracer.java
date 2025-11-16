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
        // 1.环境光: 0.05f 产生深阴影
        float umgebungsStaerke = 0.05f;
        Color umgebungslicht = hit.color().multiply(umgebungsStaerke);
                    
        // 漫反射 (产生明暗分割)
        Color diffuserTermTotal = Color.black();
        Color spiegelnderTermTotal = Color.black();
                    
        // 2. 遍历所有光源，计算每个光源的贡献
        for (Lichtquelle licht : lichtquelle) {
            // 光源方向（从交点到光源）和强度
            Vec3 lichtRichtung = licht.richtung(hit.position()).normalize();
            Color lichtIntensitaet = licht.einfallend(hit.position());

            // 3. 漫反射项（diffuser Term）
            double skalarproduktDiffus = Math.max(0, hit.normal().dot(lichtRichtung));
            float diffuserStaerke = 0.7f;  // 漫反射系数k_d
            Color diffuserTerm = hit.color()
                .multiply((float) (diffuserStaerke * skalarproduktDiffus))  // k_d * (n·l)
                .multiplyWithColor(lichtIntensitaet);  // 乘以光源强度
            diffuserTermTotal = diffuserTermTotal.add(diffuserTerm);  // 叠加漫反射

            // 4. 镜面反射项（spiegelnder Term）
            Vec3 einfallenderLichtstrahl = lichtRichtung.multiply(-1);  // 入射光方向（-l）
            Vec3 reflektierteRichtung = reflect(hit.normal(), einfallenderLichtstrahl);  // 反射方向r
            Vec3 blickRichtung = camera.position().subtract(hit.position()).normalize();  // 视线方向v

            // 反射方向与视线方向的点积（r·v）
            double skalarproduktSpiegelnd = Math.max(0, reflektierteRichtung.dot(blickRichtung));
            float spiegelnderStaerke = 2.0f;  // 镜面系数k_s
            double spiegelndExponent = 200.0;  // 高光指数n

            Color spiegelnderTerm = lichtIntensitaet
                .multiply((float) (spiegelnderStaerke * Math.pow(skalarproduktSpiegelnd, spiegelndExponent)));
            spiegelnderTermTotal = spiegelnderTermTotal.add(spiegelnderTerm);  // 叠加高光
        }

    
    // 5. 最终颜色 = 环境光 + 漫反射 + 镜面反射
    return umgebungslicht.add(diffuserTermTotal).add(spiegelnderTermTotal);
}    
}