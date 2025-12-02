
package cgg;

import tools.*;

public class Image implements tools.Image {

    private final int width;
    private final int height;
    private final double[] pixels; //存储图像，每个像素分3份

    // ---8<--- missing-implementation
    // TODO Provides storage for the image data. For each pixel in the image
    // three double values are needed to store the pixel components.
    public Image(int width, int height) {
        this.width =width;
        this.height=height;
        this.pixels= new double[width * height * 3];
    }

    // TODO Stores the RGB color components for one particular pixel addressed
    // by it's coordinates in the image.
    @Override
    public void setPixel(int x, int y, Color color) {
        //检查坐标合法性
        if ( x<0 || x>=width || y<0 || y>=height) {
            return;
        }

        int index = (y*width + x) * 3;
        pixels[index] = color.r(); //红
        pixels[index+1] = color.g(); //绿
        pixels[index+2] = color.b(); //蓝
    }


    // TODO Retrieves the RGB color components for one particular pixel addressed
    // by it's coordinates in the image.
    @Override
    public Color getPixel(int x, int y) {
        if (x<0 || x>=width || y<0 || y>=height){
            return Color.black;
        }
        int index = (y * width + x) * 3;
        return new Color(pixels[index], pixels[index + 1], pixels[index +2], index +3);
    }
    // --->8---

    public void writePng(String name) {
        ImageWriter.writePng(name,pixels,width,height);
        // TODO This call also needs to be adjusted once Image() and setPixel()
        // are implemented. Use
        // ImageWriter.writePng(String name, double[] data, int width, int height) to
        // write the image data to disk in PNG format.
    }

    public void writeHdr(String name) {
        ImageWriter.writeHdr(name,pixels,width,height);
        // TODO This call also needs to be adjusted once Image() and setPixel()
        // are implemented. Use
        // ImageWriter.writePng(String name, double[] data, int width, int height) to
        // write the image data to disk in OpenEXR format.
    }

    @Override
    public int width() {
        // TODO This is just a dummy value to make the compiler happy. This
        // needs to be adjusted such that the actual width of the Image is
        // returned.
        return width;
    }

    @Override
    public int height() {
        // TODO This is just a dummy value to make the compiler happy. This
        // needs to be adjusted such that the actual height of the Image is
        // returned.
        return height;
    }
}
