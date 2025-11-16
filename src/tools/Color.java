package tools;

/**
 * Represents a color with red, green, and blue components.
 * This class provides constants for common colors.
 */
public record Color(double r, double g, double b) {

    /** Black color (0, 0, 0). */
    public static final Color black = color(0, 0, 0);
    /** Gray color (0.5, 0.5, 0.5). */
    public static final Color gray = color(0.5, 0.5, 0.5);
    /** White color (1, 1, 1). */
    public static final Color white = color(1, 1, 1);
    /** Red color (1, 0, 0). */
    public static final Color red = color(1, 0, 0);
    /** Green color (0, 1, 0). */
    public static final Color green = color(0, 1, 0);
    /** Blue color (0, 0, 1). */
    public static final Color blue = color(0, 0, 1);
    /** Cyan color (0, 1, 1). */
    public static final Color cyan = color(0, 1, 1);
    /** Magenta color (1, 0, 1). */
    public static final Color magenta = color(1, 0, 1);
    /** Yellow color (1, 1, 0). */
    public static final Color yellow = color(1, 1, 0);

    // 工厂方法：创建Color实例（兼容原有color()调用）
    public static Color color(double r, double g, double b) {
        return new Color(r, g, b);
    }

    /**
     * Returns a string representation of the color.
     *
     * @return A string in the format "(Color: r.rr g.gg b.bb)".
     */
    @Override
    public String toString() {
        return String.format("(Color: %.2f %.2f %.2f)", r, g, b);
    }

    /**
     * 颜色乘以系数（用于调整亮度，如光照强度）
     * @param factor 亮度系数（如 0.1 表示10%亮度）
     * @return 新的颜色，分量为原分量 × factor
     */
    public Color multiply(float factor) {
        return color(r * factor, g * factor, b * factor);
    }

    /**
     * 颜色乘以系数（double版本，兼容更多场景）
     */
    public Color multiply(double factor) {
        return color(r * factor, g * factor, b * factor);
    }

    /**
     * 颜色相加（用于混合环境光和漫反射光）
     * @param other 另一种颜色（如漫反射色）
     * @return 新的颜色，分量为两颜色分量之和
     */
    public Color add(Color other) {
        return color(r + other.r, g + other.g, b + other.b);
    }

    /**
     * 颜色与衰减系数相乘（静态方法，用于光源衰减）
     */
    public static Color multiply(Color intensity, double attenuation) {
        return color(
            intensity.r * attenuation,
            intensity.g * attenuation,
            intensity.b * attenuation
        );
    }

    /**
     * 静态方法：返回黑色（兼容原有调用）
     */
    public static Color black() {
        return black;
    }

    /**
     * 颜色逐通道相乘（用于光源颜色与物体颜色混合）
     */
    public Color multiplyWithColor(Color other) {
        return color(
            this.r * other.r,
            this.g * other.g,
            this.b * other.b
        );
    }

    /**
     * 关键修复：实现clamp()方法，限制RGB分量在[0, 1]
     */
    public Color clamp() {
        double clampedR = Math.max(0.0, Math.min(1.0, r));
        double clampedG = Math.max(0.0, Math.min(1.0, g));
        double clampedB = Math.max(0.0, Math.min(1.0, b));
        return color(clampedR, clampedG, clampedB);
    }

}