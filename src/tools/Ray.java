package tools;

public class Ray {
    private final Vec3 x;
    private final Vec3 d;
    private final double tmin;
    private final double tmax;

    public Ray(Vec3 x, Vec3 d, double tmin, double tmax){
        this.x = x;
        this.d = d.normalize();
        this.tmin = tmin;
        this.tmax = tmax;
    }

    public Vec3 at(double t) {
        return x.add(d.multiply(t));
    }

    public boolean isWithinBounds(double t){
        return t >= tmin && t <= tmax;
    }

    //getter
    public Vec3 x() {
        return x; 
    }
    public Vec3 d() { 
        return d; 
    }

    public double tmin(){
        return tmin;
    }

    public double tmax(){
        return tmax;
    }

    public String origin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'origin'");
    }

    public String direction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'direction'");
    }
    
}
