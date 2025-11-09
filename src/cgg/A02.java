package cgg;

import cgg.Image;
import java.util.ArrayList;
import java.util.List;
import tools.Color;
import tools.SimpleCamera;
import tools.SimpleRayTracer;
import tools.Sphere;
import tools.Vec3;


public class A02 {

  public static void main(String[] args){

    SimpleCamera camera = new SimpleCamera(Math.PI / 3, 600, 600);

    List<Sphere> spheres = createSphereGrid(6, 6); // 调用上面的方法

    Color backgroundColor = new Color(0.1f, 0.1f, 0.1f);
    SimpleRayTracer rayTracer = new SimpleRayTracer(camera, spheres, Color.black);

   
    Image image = new Image(600,600);

    for (int y = 0; y < 600; y++) {
        for (int x = 0; x < 600; x++) {
            // 计算当前像素颜色（光线追踪核心）
            Color pixelColor = rayTracer.getColor(x, y);
            image.setPixel(x, y, pixelColor);
        }
    }

    // 输出图像
    image.writePng("a02");

    }

    /**
     * 创建网格排列的3D球体（复用A01的行列逻辑，映射到3D空间）
     * @param rows 行数（与A01一致）
     * @param cols 列数（与A01一致）
     * @return 球体列表
     */


    private static List<Sphere> createSphereGrid(int rows, int cols) {
        List<Sphere> spheres = new ArrayList<>();

        double radius = 1.3; // 球体半径（统一大小，保持网格整齐）
        double spacing = 4.0; // 球体间距（3D空间中）
        double zPos = -24; // 所有球体在同一深度（z轴，远离相机）

        // 计算网格中心偏移（让网格居中显示）
        double centerOffsetX = (cols - 1) * spacing / 2.0;
        double centerOffsetY = (rows - 1) * spacing / 2.0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 3D坐标计算（复用A01的行列索引，转换为x/y/z）
                double x = col * spacing - centerOffsetX; // 列→x轴
                double y = -(row * spacing - centerOffsetY); // 行→y轴（翻转使行顺序正确）
                Vec3 center = new Vec3(x, y, zPos); // 球体中心3D坐标

                // 复用A01的颜色逻辑（行列决定颜色，与参考图一致）
                Color color = getSphereColor(row, col, rows, cols);

                // 添加球体到列表（3D球体，替代A01的2D圆形）
                spheres.add(new Sphere(center, radius, color));
            }
        }
        return spheres;
    }

    /**
     * 复用A01的颜色生成逻辑（行控制绿色，列控制红色，蓝色固定）
     * 生成类似参考图的渐变色彩
     */
    private static Color getSphereColor(int row, int col, int totalRows, int totalCols) {
        // 红色分量随列增加（与A01一致）
        float r = (float) col / totalCols;
        // 绿色分量随行增加（与A01一致）
        float g = (float) row / totalRows;
        // 蓝色分量固定（参考图中偏暗，用0.3）
        float b = 0.3f;
        return new Color(r, g, b);
    }
}
