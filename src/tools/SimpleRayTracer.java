package tools;

import java.util.List;
import tools.Color;
import tools.Hit;
import tools.Lichtquelle;
import tools.Plane;
import tools.Ray;
import tools.Sphere;
import tools.Vec2;
import tools.Vec3;


import static tools.Functions.*;

public class SimpleRayTracer {
    private final SimpleCamera camera;
    private final List<Sphere> spheres;
    private final Color backgroundColor;
    private final List<Lichtquelle> lichtquelle;
    private final Plane ground;

    public SimpleRayTracer(
        SimpleCamera camera, 
        List<Sphere> spheres, 
        Color backgroundColor,        
        Plane ground,
        List<Lichtquelle> lichtquelle
    ){
            this.camera = camera;
            this.spheres = spheres;
            this.backgroundColor = backgroundColor;
            this.ground = ground;
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

        for (Sphere sphere : spheres){
            Hit hit = sphere.intersect(ray);
            if (hit != null && hit.t() < minT && ray.isWithinBounds(hit.t())){
                minT = hit.t();
                closestHit = hit;
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
        // 光源方向 (使用左上方光源)
        Vec3 lightDir = new Vec3(0.5, 0.5, 1).normalize();
        
        // **修复：使用固定的 View Vector (近似于 (0, 0, 1)) 来消除浮点误差**
        // 相机沿负Z轴看，物体在负Z轴，视线方向是Z轴正向
        Vec3 viewDir = new Vec3(0, 0, 1).normalize(); 
        
        // 2. 环境光: 0.05f 产生深阴影
        float ambientStrength = 0.05f;
        Color ambient = hit.color().multiply(ambientStrength);

        // 3. 漫反射 (产生明暗分割)
        double diffuseFactor = Math.max(0, hit.normal().dot(lightDir));
        float diffuseStrength = 0.7f; 
        Color diffuse = hit.color()
            .multiply((float) (diffuseStrength * diffuseFactor))
            .multiply(lightIntensity);
        diffuseTotal = diffuseTotal.add(diffuse);

        // 4. 镜面反射 (产生高光)
        Vec3 incomingLight = lightDir.multiply(-1); //入射方向
        Vec3 reflectedLight = reflect(hit.normal(), incomingLight); //反射方向
        Vec3 viewDir = camera.position().subtract(hit.position()).normalize();

        
        double specularExponent = 200.0; // 极高 Ns 产生锐利高光
        double specularFactor = Math.pow(Math.max(0, reflectedLight.dot(viewDir)), specularExponent); 
        float specularStrength = 2.0f; // 强制高光强度到 2.0f
        Color specularColor = lightIntensity
            .multiply((float)(specularStrength * specularFactor));
        specularTotal = specularTotal.add(specular); 

        // 5. 最终颜色
        return ambient.add(diffuse).add(specular);
    }   
}