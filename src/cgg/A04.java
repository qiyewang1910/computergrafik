package cgg;

import cgg.Image;
import java.util.ArrayList;
import java.util.List;
import tools.Color;
import tools.Group;
import tools.Lichtquelle;
import tools.Mat44;
import tools.Plane;
import tools.Quader;
import tools.ReinhardGlobalTmo;
import tools.Shape;
import tools.SimpleCamera;
import tools.SimpleRayTracer;
import tools.Sphere;
import tools.Vec3;


public class A04 {
    
    public static void main(String[] args){

        Vec3 cameraPos = new Vec3(23,25,-68);  // 平移，Y越大越高，Z越负越远
        Vec3 cameraTarget = new Vec3(15,10, -22); // 看向场景中心
        SimpleCamera camera = new SimpleCamera(Math.PI / 3, 600, 600, cameraPos, cameraTarget);

    
        List<Shape> scene = new ArrayList<>();

        // 循环创建多个4×4雪人矩阵
        int matrixCount = 6; // 要创建的矩阵数量（比如4个）
        double matrixSpacing = 30; // 矩阵之间的前后间距

        for (int i = 0; i < matrixCount; i++) {
            // 每个矩阵封装为独立Group
            Group snowmanMatrixGroup = new Group();

            Group blackSnowmanGroup = new Group();
            // 1. 黑色雪人组：
            Mat44 blackTrans = Mat44.scale(1, 1, 1)
                                    .multiply(Mat44.rotateY(0))
                                    .multiply(Mat44.translate(1, 0, -1));
            blackSnowmanGroup.setTransform(blackTrans);
            // 2. 白色雪人组：
            Group whiteSnowmanGroup = new Group();
            Mat44 whiteTrans = Mat44.scale(1, 1, 1)
                                    .multiply(Mat44.rotateY(0))
                                    .multiply(Mat44.translate(1.1, 0, 3.2));
            whiteSnowmanGroup.setTransform(whiteTrans);

            createSnowmanGrid(4, 4,blackSnowmanGroup, whiteSnowmanGroup);
            snowmanMatrixGroup.addChild(blackSnowmanGroup);
            snowmanMatrixGroup.addChild(whiteSnowmanGroup);

            // 创建立方体（作为矩阵的一部分）
            Quader box = new Quader(24, new Color(0.9, 0.1, 0.1)); // 6x6x6正方体，红色
            // 调整立方体在矩阵中的位置（相对于矩阵的局部坐标）
            Mat44 boxTrans = Mat44.translate(-1, 0.4, -61); // 这里的坐标是相对于当前矩阵的偏移
            box.setTransform(boxTrans);
            snowmanMatrixGroup.addChild(box); // 加入当前矩阵组

            // 平移当前矩阵：沿Z轴前后排列（i=0最前，i越大越往后）
            double zOffset = -21 + i * matrixSpacing; // 基于原Z轴偏移，叠加矩阵间距
            Mat44 matrixTrans = Mat44.translate(0, 0, zOffset);
            snowmanMatrixGroup.setTransform(matrixTrans);

            scene.add(snowmanMatrixGroup);  // 加入场景
        }

        // 创建地面平面
        Vec3 planeCenter = new Vec3(0,-300,-22); //球心位置
        double planeRadius = 300;   //球心半径
        Color planeColor = new Color(0.3, 0.3, 0.3);            
        double planeYMin = 6;  
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
        System.out.println("start rendering...");

        for (int y = 0; y < 600; y++) {
            if(y % 100 == 0){
                System.out.println("doing: "+ (y * 100 / 600 ) + "% ");
            }
            for (int x = 0; x < 600; x++){
                Color pixelColor = rayTracer.getColor(x, y);
                image.setPixel(x,y, pixelColor);
            }
        }

        new ReinhardGlobalTmo(0.005).toneMap(image);
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
