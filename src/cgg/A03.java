package cgg;

import cgg.Image;
import java.util.ArrayList;
import java.util.List;
import tools.Color;
import tools.SimpleCamera;
import tools.SimpleRayTracer;
import tools.Sphere;
import tools.Vec3;


public class A03 {
    public static void main(String[] args){
        SimpleCamera camera = new SimpleCamera(Math.PI / 3, 600, 600);

        List<Sphere> spheres = createSphereGrid(4,4);

        Vec3 planeCenter = new Vec3(0,-5,-10);
        Vec3 planeNormal = new Vec3(0,1,0.3);
        Color planeColor = new Color(0.2, 0.2, 0.2);
        Plane groundPlane = new Plane(planeCenter, planeNormal, planeColor);

        Color backgroundColor = new Color(0.1, 0.1,1);

        SimpleRayTracer rayTracer = new SimpleRayTracer(camera, spheres, backgroundColor, groundPlane);

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
     * @param rows 4
     * @param cols 4
     * @return 球体列表
     */
    private static List<Sphere> createSphereGrid(int rows, int cols){
        List<Sphere> spheres = new ArrayList<>();

        double radius = 1.5;
        double spacing = 4.0;
        double yPos = 0;

        //计算中心偏移，使矩阵居中
        double centerOffsetX = (cols - 1) * spacing / 2.0;
        double centerOffsetZ = (rows - 1) * spacing / 2.0;

        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                //XZ平面排列，col控制x轴，row控制z轴
                double x = col *  spacing -centerOffsetX;
                double z = -(row * spacing -centerOffsetZ) - 15;
                Vec3 center = new Vec3(x, yPos, z);

                Color color = getSphereColor(row, col, rows, cols);

                spheres.add(new Sphere(center, radius, color));
            }
        }
        return spheres;
    }

    private static Color getSphereColor(int row, int col, int totalRows, int totalCols){
        float r = (float) col / totalCols;
        float g = (float) row / totalRows;
        float b = 0.3;
        return new Color(r,g,b);
    }

}
