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

    List<Sphere> spheres = createSphereGrid(6, 6); 

    new Color(0.1f, 0.1f, 0.1f);
    SimpleRayTracer rayTracer = new SimpleRayTracer(camera, spheres, Color.black, null, null);

   
    Image image = new Image(600,600);

    for (int y = 0; y < 600; y++) {
        for (int x = 0; x < 600; x++) {
            
            Color pixelColor = rayTracer.getColor(x, y);
            image.setPixel(x, y, pixelColor);
        }
    }

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

        double radius = 1.5; 
        double spacing = 4.0; 
        double zPos = -24; 

        
        double centerOffsetX = (cols - 1) * spacing / 2.0;
        double centerOffsetY = (rows - 1) * spacing / 2.0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                
                double x = col * spacing - centerOffsetX; 
                double y = -(row * spacing - centerOffsetY); 
                Vec3 center = new Vec3(x, y, zPos); 

                Color color = getSphereColor(row, col, rows, cols);

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

        float r = (float) col / totalCols;
        float g = (float) row / totalRows;
        float b = 0.3f;
        return new Color(r, g, b);
    }
}
