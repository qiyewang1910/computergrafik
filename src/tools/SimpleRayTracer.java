package tools;

import java.util.List;
import tools.Color;
import tools.Hit;
import tools.Ray;
import tools.Sphere;
import tools.Vec2;
import tools.Vec3;
import static tools.Functions.*;

public class SimpleRayTracer {
    private final SimpleCamera camera;
    private final List<Sphere> spheres;
    private final Color backgroundColor;

    public SimpleRayTracer(SimpleCamera camera, List<Sphere> spheres, Color backgroundColor){
        this.camera = camera;
        this.spheres = spheres;
        this.backgroundColor = backgroundColor;
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
     * 光照模型：环境光 + 漫反射 + 镜面反射 (Phong 模型)
     */
    private Color shade(Hit hit){
        // 1. 定义光照参数
        // 光源方向 
        Vec3 lightDir = new Vec3(-0.5, 0.5, -1).normalize();
        // 观察方向 (从交点指向相机原点(0,0,0)，也就是反向的 Ray.d())
        Vec3 viewDir = hit.p().multiply(-1).normalize(); 
        
        // 2. 环境光 (Ambient)
        float ambientStrength = 0.05f;
        Color ambient = hit.color().multiply(ambientStrength);

        // 3. 漫反射 (Diffuse)
        // 漫反射强度 = max(法向量 dot 光源向量, 0)
        double diffuseFactor = Math.max(0, hit.normal().dot(lightDir));
        float diffuseStrength = 0.8f; // 调整漫反射强度
        Color diffuse = hit.color().multiply((float) (diffuseStrength * diffuseFactor));

        // 4. 镜面反射
        // a. 计算反射光方向 (R = 2 * (N dot L) * N - L)
        Vec3 incomingLight = lightDir.multiply(-1);
        Vec3 reflectedLight = reflect(hit.normal(), incomingLight);
        
        double specularExponent = 100.0; // 极高 Ns 产生锐利高光
        double specularFactor = Math.pow(Math.max(0, reflectedLight.dot(viewDir)), specularExponent); 
        
        Color specularColor = Color.white; 
        float specularStrength = 1.0f; // 保证高光可见
        Color specular = specularColor.multiply((float) (specularStrength * specularFactor));

        // 5. 最终颜色 = 环境光 + 漫反射 + 镜面反射
        return ambient.add(diffuse).add(specular);
    }   
    
}
