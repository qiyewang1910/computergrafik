package tools;


public interface Lichtquelle {
    Vec3 richtung(Vec3 punkt);
    Color einfallend(Vec3 punkt);

    boolean isPunktlicht();
    Vec3 getPosition();

    // 创建方向光源（外部包通过接口调用，无需访问内部类）
    static Lichtquelle createRichtungslicht(Vec3 richtung, Color intensitaet) {
        return new Richtungslichtquelle(richtung, intensitaet);
    }

    // 创建点光源
    static Lichtquelle createPunktlicht(Vec3 position, Color intensitaet) {
        return new Punktlichtquelle(position, intensitaet);
    }
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
        // 方向光源：返回"从交点指向光源"的方向（与存储的照射方向相反）
        return richtung.negate(); 
    }

    @Override
    public Color einfallend(Vec3 punkt) {
        return intensitaet;
    }

    // 方向光源返回false
    @Override
    public boolean isPunktlicht() {
        return false;
    }

    // 方向光源无位置，返回null
    @Override
    public Vec3 getPosition() {
        return null;
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

    // 点光源返回true
    @Override
    public boolean isPunktlicht() {
        return true;
    }

    // 返回点光源位置
    @Override
    public Vec3 getPosition() {
        return position;
    }
}