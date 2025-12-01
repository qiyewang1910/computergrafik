package tools;

import tools.Hit;
import tools.Ray;


public interface Shape {
    
    Hit intersect(Ray ray);

   
        Color getColor();
}