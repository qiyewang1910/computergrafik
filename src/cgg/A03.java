package cgg;

import cgg.Image;
import java.util.ArrayList;
import java.util.List;
import tools.Color;
import tools.Lichtquelle;
import tools.Plane;
import tools.SimpleCamera;
import tools.SimpleRayTracer;
import tools.Sphere;
import tools.Vec3;


public class A03 {
    
    public static void main(String[] args){
        SimpleCamera camera = new SimpleCamera(Math.PI / 3, 600, 600);

        List<Sphere> spheres = createSphereGrid(4,4);

        Vec3 planeCenter = new Vec3(0,-5,-10);
        double planeRadius = 50; 
        Color planeColor = new Color(0.2, 0.2, 0.2);
        double planeYMin = -6;
        Plane groundPlane = new Plane(planeCenter, planeRadius, planeColor, planeYMin);

        // 4. 背景色
        Color backgroundColor = new Color(0.1, 0.1, 1);

        // 5. 添加光源（就在这里！）
        List<Lichtquelle> lichtquellen = new ArrayList<>();  // 创建光源列表（非null）
        
        // 5.1 添加方向光源（如太阳光，从左上方向下照射）
        Vec3 lichtRichtung = new Vec3(-1, -1, -1).normalize();  // 光源方向（归一化）
        Color lichtIntensitaet = new Color(1.0f, 1.0f, 1.0f);   // 白光强度
        lichtquellen.add(Lichtquelle.createRichtungslicht(lichtRichtung, lichtIntensitaet));
        
        // 5.2 （可选）添加点光源（如灯泡，在场景上方）
        Vec3 punktLichtPos = new Vec3(0, 10, -15);  // 点光源位置（球体上方）
        Color punktLichtIntens = new Color(0.5f, 0.5f, 0.5f);  // 点光源强度
        lichtquellen.add(Lichtquelle.createPunktlicht(punktLichtPos, punktLichtIntens));

        // 6. 创建光线追踪器（传入光源列表）
        SimpleRayTracer rayTracer = new SimpleRayTracer(
            camera,
            spheres,
            groundPlane,
            backgroundColor,
            lichtquellen  // 这里传入上面创建的光源列表
        );

    Image image = new Image(600,600);
    for (int y = 0; y < 600; y++) {
        for (int x = 0; x < 600; x++){
            Color pixelColor = rayTracer.getColor(x, y);
            image.setPixel(x,y, pixelColor);
        }
    }

    image.writePng("a03");
}    

    /**
     * 创建xz轴平面上的4*4球体矩阵
     */
    private static List<Sphere> createSphereGrid(int rows, int cols) {
        List<Sphere> spheres = new ArrayList<>();
        double radius = 1.5;
        double spacing = 4.0;
        double yPos = 0;

        // 计算中心偏移，使矩阵居中
        double centerOffsetX = (cols - 1) * spacing / 2.0;
        double centerOffsetZ = (rows - 1) * spacing / 2.0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // XZ平面排列，col控制x轴，row控制z轴
                double x = col * spacing - centerOffsetX;
                double z = -(row * spacing - centerOffsetZ) - 15;
                Vec3 center = new Vec3(x, yPos, z);

                // 根据行列计算球体颜色
                Color color = getSphereColor(row, col, rows, cols);
                spheres.add(new Sphere(center, radius, color));
            }
        }
        return spheres;
    }

    // 根据行列计算球体颜色

    private static Color getSphereColor(int row, int col, int totalRows, int totalCols) {
        float r = (float) col / totalCols;
        float g = (float) row / totalRows;
        float b = 0.3f;
        return new Color(r, g, b);
    }

}
