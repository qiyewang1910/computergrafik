package tools;

import static tools.Functions.*;

// 必须显式导入AWT/ImageIO相关类
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// 实现Sampler接口
public class ImageTexture implements Sampler {

    private BufferedImage image;
    public final int width;
    public final int height;
    private final double componentScale;
    private final int components;

    /**
     * Constructs an ImageTexture from an image file.
     *
     * @param filename The path to the image file to load
     * @throws RuntimeException if the image cannot be read or is invalid
     */
    public ImageTexture(String filename) {
        try {
            File imageFile = new File(filename);
            if (!imageFile.exists()) {
                System.err.println("Image file does not exist: " + filename);
                System.exit(1);
            }
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.println("Cannot read image from: " + filename);
            e.printStackTrace();
            System.exit(1);
        }

        if (image == null) {
            System.err.println("Error reading image from: " + filename);
            System.exit(1);
        }

        // 获取图片尺寸和通道数
        width = image.getWidth();
        height = image.getHeight();
        components = image.getRaster().getNumBands();

        System.out.format(
                "texture: %s: %dx%d, components: %d\n",
                filename,
                width,
                height,
                components);

        // 计算颜色归一化系数        
        switch (image.getSampleModel().getDataType()) {
            case DataBuffer.TYPE_BYTE:
                componentScale = 255;
                break;
            case DataBuffer.TYPE_USHORT:
                componentScale = 65535;
                break;
            default:
                componentScale = 1;
                break;
        }
    }

     /**
     * 原有方法：兼容旧代码，内部复用sample方法
     */
    public Color getColor(Vec2 at) {
        return sample(at);
    }


    /**
     * 实现Sampler接口的核心方法（必须）：根据UV坐标采样纹理颜色
     * @param uv 归一化纹理坐标（u/v ∈ [0,1]）
     * @return 采样后的颜色（归一化到[0,1]）
     */

     @Override
    public Color sample(Vec2 uv) {
        // 1. 处理UV超出[0,1]：取小数部分（纹理重复平铺）
        double u = uv.u() - Math.floor(uv.u());
        double v = 1.0 - (uv.v() - Math.floor(uv.v())); // 翻转V轴，匹配图片存储方向

        // 2. 转换为像素坐标，并做边界钳位（防止越界）
        int x = (int) (u * width);
        int y = (int) (v * height);
        x = Math.max(0, Math.min(x, width - 1)); // 确保x在[0, width-1]
        y = Math.max(0, Math.min(y, height - 1)); // 确保y在[0, height-1]

        // 3. 读取像素颜色
        double[] pixelBuffer = new double[4]; // 兼容RGB/RGBA
        image.getRaster().getPixel(x, y, pixelBuffer);
        
        // 处理单通道图片（灰度→RGB）
        double r = pixelBuffer[0];
        double g = components >= 2 ? pixelBuffer[1] : r;
        double b = components >= 3 ? pixelBuffer[2] : r;

        // 4. 归一化颜色到[0,1]范围
        return new Color(r / componentScale, g / componentScale, b / componentScale, 1.0);
    }

}
