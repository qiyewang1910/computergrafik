package tools;

import static tools.Functions.*;

/**
 * Represents a 3D vector with x, y, and z components.
 * This class provides constants for common vectors and methods for accessing
 * components.
 */
public final record Vec3(double x, double y, double z) {

  /** The zero vector (0, 0, 0). */
  public static final Vec3 zero = vec3(0, 0, 0);
  /** The unit vector along the x-axis (1, 0, 0). */
  public static final Vec3 xAxis = vec3(1, 0, 0);
  /** The unit vector along the y-axis (0, 1, 0). */
  public static final Vec3 yAxis = vec3(0, 1, 0);
  /** The unit vector along the z-axis (0, 0, 1). */
  public static final Vec3 zAxis = vec3(0, 0, 1);
  /** The negative unit vector along the x-axis (-1, 0, 0). */
  public static final Vec3 nxAxis = vec3(-1, 0, 0);
  /** The negative unit vector along the y-axis (0, -1, 0). */
  public static final Vec3 nyAxis = vec3(0, -1, 0);
  /** The negative unit vector along the z-axis (0, 0, -1). */
  public static final Vec3 nzAxis = vec3(0, 0, -1);

  /**
   * Returns the x-coordinate of the vector.
   * This method is an alias for the x() method.
   *
   * @return The x-coordinate (u-component) of the vector.
   */
  public double u() {
    return x;
  }

  /**
   * Returns the y-coordinate of the vector.
   * This method is an alias for the y() method.
   *
   * @return The y-coordinate (v-component) of the vector.
   */
  public double v() {
    return y;
  }

  /**
   * Returns the z-coordinate of the vector (alias for z()).
   */
  public double w() {
    return z;
  }

  /**
   * Converts the x and y components to a 2D vector.
   */
  public Vec2 uv() {
    return vec2(x, y);
  }

  /**
   * Returns the squared length of the vector (avoids sqrt for efficiency).
   */
  public double lengthSquared() {
    return x * x + y * y + z * z;
  }

  /**
   * Returns the length (magnitude) of the vector.
   */
  public double length() {
    return Math.sqrt(lengthSquared()); // 直接计算，不依赖外部方法
  }

  /**
   * Normalizes the vector to a unit vector (length = 1).
   * @return A new Vec3 with the same direction but length 1.
   */
  public Vec3 normalize() {
    double len = length();
    if (len == 0) {
      // 处理零向量（避免除以0，返回零向量）
      return zero;
    }
    double invLen = 1.0 / len; // 计算长度的倒数（比除法更高效）
    return vec3(x * invLen, y * invLen, z * invLen);
  }

  /**
   * Multiplies the vector by a scalar.
   * @param t The scalar to multiply by.
   * @return A new Vec3 with components (x*t, y*t, z*t).
   */
  public Vec3 multiply(double t) {
    return vec3(x * t, y * t, z * t);
  }
  /**
   * Adds another vector to this vector.
   * @param other The vector to add.
   * @return A new Vec3 with components (x+other.x, y+other.y, z+other.z).
   */
  public Vec3 add(Vec3 other) {
    return vec3(x + other.x, y + other.y, z + other.z);
  }

  /**
   * Subtracts another vector from this vector.
   * @param other The vector to subtract.
   * @return A new Vec3 with components (x-other.x, y-other.y, z-other.z).
   */
  public Vec3 subtract(Vec3 other) {
    return vec3(x - other.x, y - other.y, z - other.z);
  }

  /**
   * Subtracts a scalar from each component of the vector.
   * @param c The scalar to subtract.
   * @return A new Vec3 with components (x-c, y-c, z-c).
   */
  public Vec3 subtract(double c) {
    return vec3(x - c, y - c, z - c);
  }

  /**
   * Computes the dot product with another vector.
   * @param other The other vector.
   * @return The dot product (x*other.x + y*other.y + z*other.z).
   */
  public double dot(Vec3 other) {
    return x * other.x + y * other.y + z * other.z;
  }

}