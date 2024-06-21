import java.awt.Color;

/**
 * Contains utility functions for implementing a 24-bit colouring pipeline for fractal shading.
 */
public class Colouring {
  /**
   * Get the colour along a line segment in the colour cube between c1 and c2.

   * @param c1 - The start point of the colour line
   * @param c2 - The end point of the colour line
   * @param t - A quantity 0 <= t <= 1 representing the distance along the line
   * @return - A colour with components (1 - t) * c1 + t * c2
   */
  public Color getColour(Color c1, Color c2, double t) {
    if (t < 0 || t > 1) {
      throw new IllegalArgumentException("t must be within the range 0 <= t <= 1");
    }
    int r = (int) Math.round((1 - t) * c1.getRed() + t * c2.getRed());
    int g = (int) Math.round((1 - t) * c1.getGreen() + t * c2.getGreen());
    int b = (int) Math.round((1 - t) * c1.getBlue() + t * c2.getBlue());
    return new Color(r, g, b);
  }
}
