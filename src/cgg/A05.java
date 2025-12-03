package cgg;

import cgg.Image;
import java.util.ArrayList;
import java.util.List;
import tools.Color;
import tools.Ebene;
import tools.Group;
import tools.Hit;
import tools.ImageTexture;
import tools.Lichtquelle;
import tools.Mat44;
import tools.Quader;
import tools.Ray;
import tools.Shape;
import tools.SimpleCamera;
import tools.SimpleRayTracer;
import tools.Sphere;
import tools.StarrySky;
import tools.Vec2;
import tools.Vec3;


public class A05 {
    
    public static void main(String[] args){

        System.out.println("Current working directory:" + System.getProperty("user.dir"));

        StarrySky starrySky = new StarrySky();

        Vec3 cameraPos = new Vec3(27,25,-72);  // 平移，相机位置
        Vec3 cameraTarget = new Vec3(15,8, -22); // 相机看向的目标
        SimpleCamera camera = new SimpleCamera(Math.PI / 3, 800, 800, cameraPos, cameraTarget);

    
        List<Shape> scene = new ArrayList<>();




        // 循环创建多个4×4雪人矩阵
        int matrixCount = 20; // 矩阵数量
        double matrixSpacing = 30; // 矩阵之间的间距

        for (int i = 0; i < matrixCount; i++) {
            Group snowmanMatrixGroup = new Group();

            // 1. 黑色雪人组：
            Group blackSnowmanGroup = new Group();
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

            // 创建雪人网格
            createSnowmanGrid(4, 4,blackSnowmanGroup, whiteSnowmanGroup);
            snowmanMatrixGroup.addChild(blackSnowmanGroup);
            snowmanMatrixGroup.addChild(whiteSnowmanGroup);
          

            // 平移当前矩阵：沿Z轴前后排列（i=0最前，i越大越往后）
            double zOffset = -21 + i * matrixSpacing; // 基于原Z轴偏移，叠加矩阵间距
            Mat44 matrixTrans = Mat44.translate(0, 0, zOffset);
            snowmanMatrixGroup.setTransform(matrixTrans);

            scene.add(snowmanMatrixGroup);  // 加入场景
        }
      

         // 1. 加载images文件夹下的snow图片
         ImageTexture snowTexture = null;
         Ebene slopePlane = null;
         try {
             snowTexture = new ImageTexture("images/snow.jpg");
             System.out.println("photo加载成功!");
             // 直接创建带纹理的平面
             slopePlane = new Ebene(snowTexture);
             slopePlane.setTextureScale(0.001); // 纹理密度
         } catch (Exception e) {
             System.err.println("Texture loading failed! Please check if the path is images/snow.jpg");
             System.err.println("Cause of the error:" + e.getMessage());
             // 加载失败时降级为白色平面
             slopePlane = new Ebene(new Color(1,1,1,1)); 
         }
         // 设置平面变换（20度坡度 + Y=-80平移）
         Mat44 slopeTrans = Mat44.rotateX(Math.toRadians(20))
                                 .multiply(Mat44.translate(0,-80, 0));
         slopePlane.setTransform(slopeTrans);
         scene.add(slopePlane);
        

    
        
        // 4. 背景色
        Color backgroundColor = new Color(0.04, 0.04, 0.1, 1); // 深蓝色背景

        // 5. 添加光源
        List<Lichtquelle> lichtquellen = new ArrayList<>();  
        
        // 5.1 添加方向光源（太阳光）
        Vec3 lichtRichtung = new Vec3(-5, -20, -2).normalize();  // 光源方向
        Color lichtIntensitaet = new Color(0.7f, 0.7f, 0.7f, 1f); // 光源强度 
        lichtquellen.add(Lichtquelle.createRichtungslicht(lichtRichtung, lichtIntensitaet));
        
        // 5.2 添加点光源（上方）
        Vec3 punktLichtPos = new Vec3(-5, 15, -20);  // 点光源位置（球体上方）
        Color punktLichtIntens = new Color(0.6, 0.6, 0.6, 1); // 点光源强度
        lichtquellen.add(Lichtquelle.createPunktlicht(punktLichtPos, punktLichtIntens));

        // 6. 光线追踪（传入光源列表）
        SimpleRayTracer rayTracer = new SimpleRayTracer(
            camera,
            scene,
            new Color(0.04,0.04,0.1,1), // 背景色
            lichtquellen  
        );

        //  6. 渲染图片
        Image image = new Image(800,800);
        System.out.println("start rendering...");

        for (int y = 0; y < 800; y++) {
            if(y % 100 == 0){
                System.out.println("doing: "+ (y * 100 / 800 ) + "% ");
            }
            for (int x = 0; x < 800; x++){
                
                // 关键：检测是否击中slopePlane，若是则采样纹理
                Ray ray = camera.generateRay(new Vec2(x, y));

                Color pixelColor = rayTracer.getColor(x, y);

                // 只有当击中的是背景色时，才尝试采样地面纹理
                if (isBackgroundColor(pixelColor, backgroundColor)) {
                    // 检查是否击中地面
                    Hit hit = slopePlane.intersect(ray);
                    if (hit != null) {
                        // 采样地面纹理
                        pixelColor = slopePlane.getColorAt(hit.position());
                    } else {
                        // 既没击中物体也没击中地面，显示星空
                        pixelColor = starrySky.getSkyColor(ray.direction());
                    }
                }

                image.setPixel(x,y, pixelColor);
            }
        }

        image.writePng("a05");
    }
       

    /**
     * 辅助方法：判断是否为背景色（避免浮点数精度问题）
     */
    private static boolean isBackgroundColor(Color color, Color bgColor) {
        double eps = 0.001; // 精度阈值
        return Math.abs(color.r() - bgColor.r()) < eps
                && Math.abs(color.g() - bgColor.g()) < eps
                && Math.abs(color.b() - bgColor.b()) < eps;
    }

    /**
     * 创建一个雪人组（包含上下两个球体）
     */
    private static Group createSnowman(Vec3 baseCenter, double baseRadius, Color color, boolean addBase) {
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
        

        // 3. 添加长方体基座（在雪人正下方）
        // 基座尺寸
        double baseWidth = 4;
        double baseHeight = 4;
        double baseDepth = 0.5;
        // 基座颜色：灰色（可自定义）
        Color baseColor = new Color(0.5, 0.5, 0.5, 1);
        Quader base = new Quader(baseWidth, baseHeight, baseDepth, baseColor);
        
        // 基座位置：雪人底部球体的正下方（y坐标为球体底部 - 基座高度）
        double baseY = (baseCenter.y() - baseRadius) - baseHeight/2.0;
        Mat44 baseTrans = Mat44.translate(
            baseCenter.x(),  // 与雪人x坐标一致
            baseY,           // 位于雪人底部下方
            baseCenter.z()   // 与雪人z坐标一致
        );

        base.setTransform(baseTrans);
        snowman.addChild(base); // 将基座添加到雪人组

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
                Color color = (col % 2 == 0) ? new Color(0.01, 0.01, 0.01, 1) : new Color(1,1,1,1);
            
                // 创建雪人组并添加到对应组
                Group snowman = createSnowman(baseCenter, baseRadius, color, col % 2 != 0);
                if (col % 2 == 0) {
                    blackGroup.addChild(snowman);
                } else {
                    whiteGroup.addChild(snowman);
                }
            }
        }
    }
}
