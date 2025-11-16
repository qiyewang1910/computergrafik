package tools;

import static tools.Functions.*;

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
        double f = factor;
        return color(r * f, g * f, b * f);
    }


    /**
     * 颜色相加（用于混合环境光和漫反射光）
     * @param other 另一种颜色（如漫反射色）
     * @return 新的颜色，分量为两颜色分量之和
     */
    public Color add(Color other) {
        return color(r + other.r, g + other.g, b + other.b);
    }

    public static Color multiply(Color intensitaet, double attenuation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'multiply'");
    }

    public static Color black() {
        return new Color(0,0,0);
    }

    public Color multiplyWithColor(Color other) {
        return new Color(
            this.r * other.r,
            this.g * other.g,
            this.b * other.b
        );
    }

}