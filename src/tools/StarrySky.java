package tools;

import tools.Color;
import tools.Random;
import tools.Vec3;

public class StarrySky{
    private Random random = Random.generator;
    private int starCount = 500;
    private Vec3[] starDirections; // 存储星星的方向和亮度
    private float[] starBrightness; // 星星亮度

    //初始化星星 随机生成一批星星的方向和亮度
    public StarrySky(){
        starDirections = new Vec3[starCount];
        starBrightness = new float[starCount];

        for(int i = 0; i < starCount; i++){
            double theta = random.nextFloat() * Math.PI;
            double phi = Math.PI * random.nextDouble() * 2;

            double x = Math.sin(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.sin(phi);
            double z = Math.cos(theta);

            starDirections[i] = new Vec3(x, y, z).normalize();
            //随即亮度 星星越亮越接近1
            starBrightness[i] = random.nextFloat() * 0.5f + 0.2f;
        }
    }

    /**
     * 根据光线方向获取星空颜色
     * @param rayDirection 相机发出的光线方向（单位向量）
     * @return 星空颜色（背景+星星）
     */
    public Color getSkyColor(Vec3 rayDirection){
        // 深蓝色夜空底色
        Color nightSky = new Color(0.01, 0.01, 0.025, 1.0);

        // 检测光线方向是否指向星星
        for (int i = 0; i < starCount; i++) {
            // 计算光线与星星方向的点积（越接近1，方向越一致）
            double dotProduct = rayDirection.dot(starDirections[i]);
            // 阈值控制星星大小（值越大，星星越小越密集）
            if (dotProduct > 0.999997) {
                float b = starBrightness[i];
                // 星星颜色：白色/浅黄色
                return new Color(b, b, b + 0.1f, 1.0f);
            }
        }

        return nightSky;
    }
}