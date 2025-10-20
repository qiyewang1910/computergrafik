
package cgg;

import tools.*;

// Represents the contents of an image. Provides the same color for all pixels.
public class ConstantColor implements Sampler {
    private final Color color;

    // Initializes color.
    public ConstantColor(Color color) {
        this.color = color;
    }

    // Returns the color for the given position. All positions return the same
    // constant color.
    @Override
    public Color getColor(Vec2 point) {
        return color;
    }
}
