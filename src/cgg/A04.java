package cgg;

import cgg.Image;
import java.util.ArrayList;
import java.util.List;
import tools.Color;
import tools.Group;
import tools.Lichtquelle;
import tools.Plane;
import tools.Shape;
import tools.SimpleCamera;
import tools.SimpleRayTracer;
import tools.Sphere;
import tools.Vec3;


public class A04 {
    
    public static void main(String[] args){

        Vec3 cameraPos = new Vec3(25,20,-50);  // 平移，Y越大越高，Z越负越远
        Vec3 cameraTarget = new Vec3(14,5, -16); // 看向场景中心

        SimpleCamera camera = new SimpleCamera(Math.PI / 3, 600, 600, cameraPos, cameraTarget);

        // 拆分黑白小人作为组
        List<Shape> scene = new ArrayList<>();

        Group blackSnowmanGroup = new Group();
        Group whiteSnowmanGroup = new Group();

        createSnowmanGrid(4, 4,blackSnowmanGroup, whiteSnowmanGroup);

        scene.add(blackSnowmanGroup);
        scene.add(whiteSnowmanGroup);

        Vec3 planeCenter = new Vec3(0,-300,-22); //球心位置
        double planeRadius = 300;   //球心半径
        Color planeColor = new Color(0.3, 0.3, 0.3);
        double planeYMin = 5;  
        Plane groundPlane = new Plane(planeCenter, planeRadius, planeColor, planeYMin);
        scene.add(groundPlane); // 地面加入场景

        // 4. 背景色
        Color backgroundColor = new Color(0, 0, 0);

        // 5. 添加光源
        List<Lichtquelle> lichtquellen = new ArrayList<>();  // 创建光源列表
        
        // 5.1 添加方向光源（太阳光）
        Vec3 lichtRichtung = new Vec3(-3, -8, -2).normalize();  // 光源方向
        Color lichtIntensitaet = new Color(1f, 1f, 1f);   
        lichtquellen.add(Lichtquelle.createRichtungslicht(lichtRichtung, lichtIntensitaet));
        
        // 5.2 添加点光源（灯泡，在场景上方）
        Vec3 punktLichtPos = new Vec3(5, 15, -25);  // 点光源位置（球体上方）
        Color punktLichtIntens = new Color(2, 2, 2); 
        lichtquellen.add(Lichtquelle.createPunktlicht(punktLichtPos, punktLichtIntens));

        // 6. 光线追踪（传入光源列表）
        SimpleRayTracer rayTracer = new SimpleRayTracer(
            camera,
            scene,
            backgroundColor,
            lichtquellen  
        );

        Image image = new Image(600,600);
        for (int y = 0; y < 600; y++) {
            for (int x = 0; x < 600; x++){
                Color pixelColor = rayTracer.getColor(x, y);
                image.setPixel(x,y, pixelColor);
            }
        }

        image.writePng("a04");
    }    



    private static Group createSnowman(Vec3 baseCenter, double baseRadius, Color color) {
        Group snowman = new Group();
        
        // 下大球（原球体）
        Sphere baseSphere = new Sphere(baseCenter, baseRadius, color);
        snowman.addChild(baseSphere);
        
        // 上小球（半径为大球的1/2，位置在正上方）
        double topRadius = 1.4;
        Vec3 topCenter = new Vec3(
            baseCenter.x(), 
            baseCenter.y() + baseRadius + topRadius -0.5, // y轴偏移：大球半径+小球半径
            baseCenter.z()
        );
        Sphere topSphere = new Sphere(topCenter, topRadius, color);
        snowman.addChild(topSphere);
        
        return snowman;
    }

    private static void createSnowmanGrid(int rows, int cols, Group blackGroup, Group whiteGroup) {
        double baseRadius = 1.8; // 保留原大球半径
        double spacing = 4.6;    // 保留原间距
        double yPos = baseRadius; // 下大球的y位置（原球体y坐标）

        // 中心偏移 保持矩阵居中不变
        double centerOffsetX = (cols - 1) * spacing / 2.0;
        double centerOffsetZ = (rows - 1) * spacing / 2.0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 计算下大球位置（原球体位置，不变）
                double x = col * spacing - centerOffsetX;
                double z = -(row * spacing - centerOffsetZ) - 21;
                Vec3 baseCenter = new Vec3(x, yPos, z);

                // 按列设置颜色（偶数列黑，奇数列白）
                Color color = (col % 2 == 0) ? new Color(0.02,0.02,0.02) : new Color(1,1,1);
                // 创建雪人组并添加到对应组
                Group snowman = createSnowman(baseCenter, baseRadius, color);
                if (col % 2 == 0) {
                    blackGroup.addChild(snowman);
                } else {
                    whiteGroup.addChild(snowman);
                }
            }
        }
    }
 
}
