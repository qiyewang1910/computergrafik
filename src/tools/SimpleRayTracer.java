package tools;

import java.util.List;
import tools.Color;
import tools.Hit;
import tools.Ray;
import tools.Sphere;
import tools.Vec2;

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
     * 光照模型：环境光 + 漫反射
     */

    private Color shade(Hit hit){
        Vec3 lightDir = new Vec3(1,1,0.7).normalize();
        //环境光：球体颜色 * 0.1
        Color ambient = hit.color().multiply(0.1f);
        //漫反射： 球体颜色 * 0.9 * max(法向量 光源向量， 0)
        double diffuseFactor = Math.max(0, hit.normal().dot(lightDir));
        Color diffuse = hit.color().multiply((float) (0.9 * diffuseFactor));
        //最终颜色 = 环境光+漫反射
        return ambient.add(diffuse);
    }  

    
}
