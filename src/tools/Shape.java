package tools;



public interface Shape {
    
    Hit intersect(Ray ray);

   
        Color getColor();
}