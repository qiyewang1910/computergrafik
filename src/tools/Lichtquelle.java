package tools;

import tools.Color;
import tools.Vec3;

public interface Lichtquelle {
    Vec3 richtung(Vec3 punkt);
    Color einfallend(Vec3 punkt);
}

// 平行光源
class Richtungslichtquelle implements Lichtquelle {
    private final Vec3 richtung;
    private final Color intensitaet;

    public Richtungslichtquelle(Vec3 richtung, Color intensitaet) {
        
        this.richtung = richtung.normalize(); 
        this.intensitaet = intensitaet;
    }

    @Override
    public Vec3 richtung(Vec3 punkt) {
        return richtung;
    }

    @Override
    public Color einfallend(Vec3 punkt) {
        return intensitaet;
    }
}

// 点光源
class Punktlichtquelle implements Lichtquelle {
    private final Vec3 position;
    private final Color intensitaet;

    public Punktlichtquelle(Vec3 position, Color intensitaet) {
        this.position = position;
        this.intensitaet = intensitaet;
    }

    @Override
    public Vec3 richtung(Vec3 punkt) {
        Vec3 vecVonPunktZuLicht = position.subtract(punkt); 
        return vecVonPunktZuLicht.normalize(); 
    }

    @Override
    public Color einfallend(Vec3 punkt) {
        Vec3 abstandsVec = position.subtract(punkt); 
        double abstand = abstandsVec.length(); 
        double daempfung = 1.0 / (abstand * abstand + 1e-6);
        return intensitaet.multiply((float) daempfung);
    }
}