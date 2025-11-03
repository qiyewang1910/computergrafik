package cgg;

import java.util.ArrayList;
import java.util.List;
import tools.Color;
import tools.Ray;
import tools.Vec3;

public class A02 {

    // 1.复用a01的信息
    private static final int width = 600;
    private static final int height = 600;

    private static final int rows = 6;
    private static final int cols = 6;
    private static final int baseRadius = 32;
    private static final int radiusIncrement = 6;
    private static final int borderWidth = 1;

    private static final int colSpacing = 80;
    private static final int rowSpacing = 85;

    //2.光线追踪
    //相机位置
    private static final Vec3 zAxis = new Vec3(0,0,1);
    private static final Vec3 nzAxis = new Vec3(0, 0, -1);
    private static final Color white = new Color(1, 1, 1);

    public static void main(String[] args){
        //初始化像素数组
        double[] pixels = new double[width*height*3];
        //复用a01的6*6网格
        List<Sphere> spheres = createSphereGrid();

        //遍历每个像素，通过光线追踪计算颜色
        for (int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                Ray ray = generateCameraRay(x,y);
                Color pixelColor = traceRay(ray, spheres, new Color(0.0f, 0.0f, 0.0f));
                //将颜色写入像素数组
                int pixelIndex = (y * width + x) * 3;
                pixels[pixelIndex] = pixelColor.r();
                pixels[pixelIndex + 1] = pixelColor.g();
                pixels[pixelIndex + 2] = pixelColor.b(); 
            }
        }

        image.writePng("a02");        
    }

    private static List<Sphere> createSphereGrid(){
        List<Sphere> spheres = new ArrayList<>();

        for (int row = 0; row < A01.rows; row++) {
            for (int col = 0; col < A01.cols; col++){
                Color sphereColor =  A01.getCircleColor(row, col);

                int[] center2D = A01.getCircleCenter(row, col);
                int centerX = center2D[0];
                int centerY = center2D[1];
                Vec3 sphereCenter = new Vec3(centerX, centerY, 0);

                int sphereRadius = A01.getCircleRadius(col);

                spheres.add(new Sphere(sphereCenter, sphereRadius, sphereColor));

            }
        }
        return spheres;
    }

}
