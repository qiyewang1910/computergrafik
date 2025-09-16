
package cgg;

import tools.*;
import static tools.Color.black;
import static tools.Functions.*;


public class A01 {

  public static void main(String[] args) {
    int width = 400;
    int height = 400;

    Color magenta = new Color(1.0,0.0,1.0);
    var constant = new ConstantColor(magenta);
    var drawer = new DiscLiveStart();

    drawer.Circle(width, height, "magenta_circle", constant);
  } 

  public static class DiscLiveStart {
    public int y;
    
        public void Circle(int width, int height, String fileName, ConstantColor constant){
          var image = new Image(width, height);
    
          // 圆的参数：中心在图像中间，半径为120
          double centerX = width / 2.0;
          double centerY = height / 2.0;
          double radius = 120;

    
          //圆的参数，中心在图像中间，半径为120
          for (int x=0; x !=width; x++){
            for (int y=0; y !=height; y++){
              // 计算当前像素到圆心的距离平方
              double dx = x - centerX;
              double dy = y - centerY;
              double distanceSquared = dx * dx + dy * dy;

              if(distanceSquared <= radius * radius){
                image.setPixel(x, y, constant.getColor(vec2(x,y)));
              } else{
                image.setPixel(x, y, black);
              } 
            }            
          }     
          // 在画完所有像素后，保存图片
          image.writePng("a01-1");
        }
  }
}
