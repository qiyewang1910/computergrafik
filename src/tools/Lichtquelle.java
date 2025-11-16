package tools;

public class Lichtquelle {
    Vec3 direction(Vec3 x);
    Color incoming(Vec3 x);

    //内部类 方向光源
    static class Richtungslichtquelle implements Lichtquelle{
        private final Vec3 richtung;
        private final Color intensitaet;

        public Richtungslichtquelle(Vec3 richtung, Color intensitaet){
            this.richtung = Vec3.normalize(richtung);
            this.intensitaet = intensitaet;
        }

        @Override
        public Vec3 direction(Vec3 x){
            return this.richtung;
        }

        @Override
        public Color incoming(Vec3 x){
            return this.intensitaet;
        }
    }

    //点光源
    static class Punktlichtquelle implements Lichtquelle{
        private final Vec3 position;
        private final Color intensitaet;

        public Punktlichtquelle(Vec3 position, Color intensitaet){
            this.position = position;
            this.intensitaet = intensitaet;
        }

        @Override
        public Vec3 direction(Vec3 x){
            Vec3 vecFromXToLight = Vec3.subtract(this.position, x);
            return Vec3.normalize(vecFromXToLight);
        }

        @Override
        public Color incoming(Vec3 x){
            double distance = Vec3.length(Vec3.subtract(this.position, x));
            double attenuation = 1.0 / (distance * distance);
            return Color.multiply(this.intensitaet, attenuation);
        }
    }
}
