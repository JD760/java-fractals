package chunk;

import complex.Complex;
import java.awt.Color;

/**
 * Contains a collection of colouring methods for custom colour schemes.
 */
public class Colouring {
  private static final double log2 = Math.log(2);

  /**
   * Generic colouring method allowing full customisation of all colour parameters.

   * @param iterations - the number of iterations before the pixel escapes
   * @param z - the location in complex space represented by the center of the pixel
   * @param freqR - frequency of repetition of the red channel
   * @param freqG - frequency of repetition of the green channel
   * @param freqB - frequency of repetition of the blue channel
   * @param center - the center of the colour channel
   * @param delta - the maximum variation such that center + delta = 255
   * @return - a Color object representing the colour the pixel should be drawn
   */
  public static Color continuousColouring(
      int iterations, Complex z, double freqR, double freqG, double freqB, int center, int delta) {
    // to create a continuous colour scheme we need to approximate exactly how many
    // iterations it takes for a pixel to escape - we must use this index to avoid
    // colour bands from discrete integer values
    double index = iterations + 1 - ((log2 / z.magnitude()) / log2);
    return new Color(
      (int) Math.abs((Math.sin(freqR * index + 4) * center) + delta),
      (int) Math.abs((Math.sin(freqG * index + 2) * center) + delta),
      (int) Math.abs((Math.sin(freqB * index + 1) * center) + delta)
    );
  }

  /**
   * Simple colouring method that uses sample values and only requires minimal parameters.

   * @param iterations - the iteration count for the provided pixel
   * @param z - the point in complex space representing the pixel
   * @return - the Color the pixel should be drawn as
   */
  public static Color continuousColouring(int iterations, Complex z) {
    return continuousColouring(iterations, z, 0.01, 0.016, 0.013, 230, 25);
  }
}
