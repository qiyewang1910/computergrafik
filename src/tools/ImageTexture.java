package tools;

import static tools.Functions.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.println("Cannot read image from: " + filename);
            System.exit(1);
        }

        if (image == null) {
            System.err.println("Error reading image from: " + filename);
            System.exit(1);
        }

        width = image.getWidth();
        height = image.getHeight();
        components = image.getRaster().getNumBands();

        System.out.format(
                "texture: %s: %dx%d, components: %d\n",
                filename,
                width,
                height,
                components);

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

    public Color getColor(Vec2 at) {
        // if (u < 0 || u > 1 || v < 0 || v > 1)
        // return black;

        double u = at.u();
        double v = 1.0 - at.v();
        int x = (int) ((u - Math.floor(u)) * width);
        int y = (int) ((v - Math.floor(v)) * height);

        double[] pixelBuffer = new double[4];
        image.getRaster().getPixel(x, y, pixelBuffer);
        Color color = color(pixelBuffer[0], pixelBuffer[1], pixelBuffer[2]);
        return divide(color, componentScale);
    }
}
