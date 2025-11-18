package tools;

import java.util.ArrayList;
import java.util.List;

public class Group implements Shape { // 必须实现Shape接口
    private List<Shape> children = new ArrayList<>();

    // 添加子形状（参数为Shape，兼容所有实现类）
    public void addChild(Shape child) { // 关键：参数是Shape，不是Sphere
        children.add(child);
    }

    // 实现intersect方法（遍历子形状检测交点）
    @Override
    public Hit intersect(Ray ray) {
        Hit closestHit = null;
        double minT = Double.POSITIVE_INFINITY;
        for (Shape child : children) {
            Hit hit = child.intersect(ray);
            if (hit != null && hit.t() < minT && hit.t() > 0) {
                minT = hit.t();
                closestHit = hit;
            }
        }
        return closestHit;
    }
}