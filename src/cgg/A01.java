
package cgg;

import tools.*;
import static tools.Color.black;
import static tools.Color.gray;
import static tools.Color.red;
import static tools.Color.green;
import static tools.Color.blue;
import static tools.Color.cyan;
import static tools.Color.magenta;
import static tools.Color.yellow;


public class A01 {

  public static void main(String[] args) {
    int width = 600;
    int height = 600;

    DiscLiveStart drawer = new DiscLiveStart();
    drawer.drawGridOfCircles(width, height, "6*6_circle", 6, 6);
  } 
    
  public static class DiscLiveStart {
    private Color[] rowColors = {
        red,
        green,
        blue,
        cyan,
        magenta,
        yellow
    };  

    public void drawGridOfCircles(int width, int height, String fileName, int rows, int cols){
      var image = new Image(width, height);

      for (int x=0; x<width; x++){
        for (int y=0; y<height; y++) {
          image.setPixel(x, y, black);
        }
      }

      // 计算网格参数
      double cellWidth = width / (double)(cols+1);
      double cellHeight = height / (double)(rows+1);
      double radius = Math.min(cellWidth, cellHeight)/2.5;
      double borderThickness = 1; //边框厚度


      //圆的参数，中心在图像中间，半径为120
      for (int row = 0; row < rows; row++){
        Color fillColor = rowColors[row];

        for (int col=0; col<cols; col++){
          double centerX = cellWidth * (col + 1);
          double centerY = cellHeight * (row + 1);

          drawCircleWithBorder(
            image, 
            centerX, 
            centerY, 
            radius, 
            borderThickness, 
            fillColor, 
            gray,
            black
          ); 
        }            
      }     
      // 在画完所有像素后，保存图片
      image.writePng("a01-2");
    }

    //绘制单个圆形的辅助方法
    private void drawCircleWithBorder(
      Image image, double centerX, double centerY, 
      double radius, double borderThickness, 
      Color fillColor, Color bordeColor,  Color bgColor
    ){
      int minX = (int) (centerX - radius - borderThickness);
      int maxX = (int) (centerX + radius + borderThickness);
      int minY = (int) (centerY - radius - borderThickness);
      int maxY = (int) (centerY + radius + borderThickness);
     

      //内圆半径=外圆半径-边框厚度
      double innerRadius = radius - borderThickness;
      double outerRadiusSquared = radius * radius;
      double innerRadiusSquared = innerRadius * innerRadius;                                  

      //遍历图像所有像素
      for (int x = Math.max(0, minX); x< Math.min(image.width(), maxX); x++) {
        for (int y=Math.max(0, minY); y < Math.min(image.height(), maxY); y++) {
          //计算像素到圆心的距离平方
          double dx = x-centerX;
          double dy = y-centerY;
          double distanceSquared = dx * dx + dy * dy;

          //根据距离设置像素颜色
          if(distanceSquared <= outerRadiusSquared){
            //圆形区域内
            if(distanceSquared > innerRadiusSquared){
            //边框灰色
              image.setPixel(x, y, bordeColor);
            }else{
              //如果在内部填充区域内，画填充颜色
              image.setPixel(x, y, fillColor);
            }
          }
        }
      }
    }
  }       
}
