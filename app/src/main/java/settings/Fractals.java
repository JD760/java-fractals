package settings;

/**
 * Set the type of fractal to use when processing pixel data.
 */
public enum Fractals {
  MANDELBROT,
  DIVERGENCE_SCHEME,
  JULIA,
  JULIA_DIVERGENCE;

  /**
   * Return a detailed string for each enumeration item that is more user-friendly
   * for use in the GUI.

   * @param mode - the item to convert to its' localised name
   * @return - the localised name representing the item mode
   */
  public static String toString(Fractals mode) {
    switch (mode) {
      case MANDELBROT:
        return "Mandelbrot Set";
      case DIVERGENCE_SCHEME:
        return "Mandelbrot Set (divergence scheme)";
      case JULIA:
        return "Julia Set";
      case JULIA_DIVERGENCE:
        return "Julia Set (divergence scheme)";
      default:
        return "";
    }
  }

  /**
   * Return the enum element corresponding to the string mode, else return null.

   * @param mode - a string that may or may not correspond to an enum item
   * @return - the enum item if there exists one matching the string mode, else null
   */
  public static Fractals getElement(String mode) {
    switch (mode) {
      case "MANDELBROT":
        return Fractals.MANDELBROT;
      case "DIVERGENCE_SCHEME":
        return Fractals.DIVERGENCE_SCHEME;
      case "JULIA":
        return Fractals.JULIA;
      case "JULIA_DIVERGENDE":
        return Fractals.JULIA_DIVERGENCE;
      default:
        return null;
    }
  }
}