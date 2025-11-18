// tools/Shape.java（新建文件，仅包含接口定义）
package tools;

import tools.Hit;
import tools.Ray;

// 定义Shape接口，所有可被光线检测的形状都要实现它
public interface Shape {
    // 必须实现的方法：检测光线与形状的交点
    Hit intersect(Ray ray);
}