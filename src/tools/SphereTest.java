package tools;

public class SphereTest {
    public static void main(String[] args){

        Ray r1 = new Ray(new Vec3(0,0,0), new Vec3(0,0,-1), 0.001, Double.POSITIVE_INFINITY);

        //r1
        System.out.println("r1:");
        System.out.println("r1 origin:" + r1.origin());
        System.out.println("r1 Richtung:" + r1.direction());
        System.out.println();

        //r1&s1
        Sphere s1 = new Sphere(new Vec3(0,0,-2), 1, new Color(0,0,0));
        System.out.println("s1 und r1 schneiden ");
        System.out.println(s1.intersect(r1));
        System.out.println();

        //r1&s2
        Sphere s2 = new Sphere(new Vec3(0,-1,-2), 1, new Color(0,0,0));
        System.out.println("s2 und r1 schneiden ");
        System.out.println(s2.intersect(r1));
        System.out.println();

        //r1&s3
        Sphere s3 = new Sphere(new Vec3(0,0,0),1, new Color(0,0,0));
        System.out.println("s3 und r1 schneiden ");
        System.out.println(s3.intersect(r1));
        System.out.println();

        //r2&s1
        Ray r2 = new Ray(new Vec3(0,0,0), new Vec3(0,1,-1), 0.001, Double.POSITIVE_INFINITY);
        System.out.println("s1 und r2 schneiden ");
        System.out.println(s1.intersect(r2));
    }
}
