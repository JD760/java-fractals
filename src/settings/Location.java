package settings;

import complex.Complex;

/**
 * Represents a specific location in the canvas, along with the settings
 * of the viewer at that point.
 */
public class Location {
  public Complex center;
  public Complex seed;
  public double scale;
  public int maxIterations;
  public Fractals mode;

  /**
   * Create a new Location object, with the appropriate settings.

   * @param center - the center point of the fractal
   * @param scale - a scale of n means a unit square becomes a square with side length n
   * @param maxIterations - the maximum iterations before concluding that a point converges
   * @param mode - the fractal to draw on the canvas
   */
  public Location(Complex center, Complex seed, double scale, int maxIterations, Fractals mode) {
    this.center = center;
    this.seed = seed;
    this.scale = scale;
    this.maxIterations = maxIterations;
    this.mode = mode;
  }

  public Location() {
    this(new Complex(), new Complex(-0.32, 0.21), 1.0, 1000, Fractals.MANDELBROT);
  }

  @Override
  public String toString() {
    StringBuilder outputStr = new StringBuilder();
    outputStr.append(center.re() + ",");
    outputStr.append(center.im() + ",");
    outputStr.append(seed.re() + ",");
    outputStr.append(seed.im() + ",");
    outputStr.append(scale + ",");
    outputStr.append(maxIterations + ",");
    outputStr.append(mode.toString());
    return outputStr.toString();
  }

  /**
   * Attempts to extract the settings from a string representation of a Location.

   * @param location - a string which should be the output from some Location.toString()
   * @return - true if the Location was successfully parsed, false otherwise.
   */
  public boolean parseString(String location) {
    Complex tempCenter;
    Complex tempSeed;
    double tempScale;
    int tempMaxIterations;
    Fractals tempMode;

    String[] settings = location.split(",");
    if (settings.length != 7) {
      return false;
    }

    try {
      double re = Double.parseDouble(settings[0]);
      double im = Double.parseDouble(settings[1]);
      tempMaxIterations = Integer.parseInt(settings[5]);
      tempScale = Double.parseDouble(settings[4]);
      tempCenter = new Complex(re, im);
      tempSeed = new Complex(Double.parseDouble(settings[2]), Double.parseDouble(settings[3]));
    } catch (NumberFormatException e) {
      return false;
    }

    tempMode = findMode(settings[4]);
    if (tempMode == null) {
      return false;
    }

    center = tempCenter;
    seed = tempSeed;
    scale = tempScale;
    maxIterations = tempMaxIterations;
    mode = tempMode;
    return true;
  }

  private Fractals findMode(String input) {
    for (Fractals mode : Fractals.values()) {
      if (mode.toString() == input) {
        return mode;
      }
    }
    return null;
  }
}
