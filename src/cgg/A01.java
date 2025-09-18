
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
    int width = 500;
    int height = 500;

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
      double borderThickness = 1; //边框厚度


      //圆的参数，中心在图像中间，半径为120
      for (int row = rows - 1; row >= 0; row--){
        Color baseColor = rowColors[row];

        for (int col = cols - 1; col >= 0; col--){
          // 计算混合系数 t，从左到右，t从0逐渐增加到1
          // 这里使用 (cols - 1 - col) 是为了让最左边（col=0）的 t=1，最右边（col=5）的 t=0
          // 这样可以确保颜色从左到右逐渐变浅。
          double mixFactor = ((double)(col) / (cols - 1))*0.8;

          // 根据混合系数 t 计算新的填充颜色
          Color fillColor = new Color(
            baseColor.r() + (1.0 - baseColor.r()) * mixFactor,
            baseColor.g() + (1.0 - baseColor.g()) * mixFactor,
            baseColor.b() + (1.0 - baseColor.b()) * mixFactor
          );


          //动态计算半径，让它随着列数增大
          double radius = (Math.min(cellWidth, cellHeight) / 2.4) + (col*5);
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
      image.writePng("a01-3");
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
