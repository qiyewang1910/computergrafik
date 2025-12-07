package tools;

public class ColorSampler implements Sampler {

  private final Color color;

  public ColorSampler(Color color) {
    this.color = color;
  }

  @Override
  public Color sample(Vec2 uv) {
    return color;
  }
}
