
package cgg;

import tools.*;
import static tools.Color.black;


public class A01 {

  public static void main(String[] args) {
    int width = 600;
    int height = 600;

    Color magenta = new Color(1.0,0.0,1.0);
    new ConstantColor(magenta);
    new DiscLiveStart();

    DiscLiveStart drawer = new DiscLiveStart();

    drawer.drawGridOfCircles(width, height, "6*6_circle", 6, 6);
  } 

  public static class DiscLiveStart {
    public int y;
    
        public void drawGridOfCircles(int width, int height, String fileName, int rows, int cols){
          var image = new Image(width, height);
    
          // 圆的参数：中心在图像中间，半径为120
          double cellWidth = width / (double)(cols+1);
          double cellHeight = height / (double)(rows+1);
          double radius = Math.min(cellWidth, cellHeight)/4;

    
          //圆的参数，中心在图像中间，半径为120
          for (int row = 0; row < rows; row++){
            for (int col=0; col<cols; col++){
              double centerX = cellWidth * (col + 1);
              double centerY = cellHeight * (row + 1);

              drawCircle(image, centerX, centerY, radius, black, null); 
            }            
          }     
          // 在画完所有像素后，保存图片
          image.writePng("a01-2");
        }

        //绘制单个圆形的辅助方法
        private void drawCircle(Image image, double centerX, double centerY, double radius, Color circleColor, Color bgColor){
          int width = image.width();
          int height = image.height();

          //遍历图像所有像素
          for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
              //计算像素到圆心的距离平方
              double dx = x-centerX;
              double dy = y-centerY;
              double distanceSquared = dx * dx + dy + dy;

              //根据距离设置像素颜色
              if(distanceSquared <= radius * radius){
                image.setPixel(x,y,circleColor);
              }else{
                if (image.getPixel(x, y) == null){
                  image.setPixel(x, y, bgColor);
                }
              }
            }
          }
        }
  }
}
