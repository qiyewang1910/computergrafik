
package cgg;

import tools.Color;

public class A03 {
  public static void main(String[] args) {
    int width = 600;
    int height = 600;
    Image image = new Image(width, height);

    // 1. background black
    for (int x = 0; x < width; x++){
      for (int y = 0; y < image.height(); y++){
        image.setPixel(x, y, Color.black);
      }
    }

    //2. 6*6 Rund
    int rows = 6;
    int cols = 6;
    int baseRadius = 32;
    int radiusIncrement = 6;
    int borderWidth = 1;

    int colSpacing = 80;
    int rowSpacing = 85;
    

    //3. 绘制矩阵
    for (int row = 0; row < rows; row++){
      for (int col= 0; col< cols; col++){
        int centerX = 100 + col * colSpacing;  // 第1列X=100，每列+100
        int centerY = 80 + row * rowSpacing;   // 第1行Y=80，每行+80

        // 半径随列数变大：第1列=15，第2列=25，…，第6列=65
        int currentRadius = baseRadius + col * radiusIncrement;

        Color color = getCircleColor(row, col, rows, cols);

        drawCircleBorder (image, centerX, centerY, currentRadius, borderWidth, Color.white);
        drawCircle (image, centerX, centerY, currentRadius, color);
      }
    }

    image.writePng("a01");

  }  
    
  //绘制单个圆
  private static void drawCircle(Image image, int centerX, int centerY, int radius, Color color) {
    for(int x =0; x< image.width(); x++){
      for (int y = 0; y < image.height(); y++){
        int dx = x - centerX;
        int dy = y - centerY;
        int distanceSquared = dx * dx + dy * dy;

        if (distanceSquared <= radius * radius) {
          image.setPixel(x, y, color);
        }
      }
    }  
  }

  // 绘制白色边框（外层圆的边缘部分）
  private static void drawCircleBorder(Image image, int centerX, int centerY, 
                                      int radius, int borderWidth, Color borderColor) {
    int outerRadius = radius + borderWidth; // 外层圆半径（包含边框）
    for(int x = 0; x < image.width(); x++){
      for (int y = 0; y < image.height(); y++){
        int dx = x - centerX;
        int dy = y - centerY;
        int distanceSquared = dx * dx + dy * dy;
        // 像素在边框范围内：外层圆内，但内层圆外（只保留边缘）
        if (distanceSquared <= outerRadius * outerRadius 
            && distanceSquared > radius * radius) {
          image.setPixel(x, y, borderColor); // 白色边框
        }
      }
    }
  }

  private static Color getCircleColor(int row, int col, int totalRows, int totalCols){
    float red = (float) col / totalCols;
    float green = (float) row / totalRows;
    float blue = 0.3f;
    return new Color(red, green, blue);
  }

} 