package settings;

/**
 * Contains the current colour channel settings to control how the fractal is rendered.
 */
public class ColorSettings {
  public double redFreq;
  public double greenFreq;
  public double blueFreq;
  public int delta;
  public int center;

  /**
   * Set up new colouring settings with sample defaults.
   */
  public ColorSettings() {
    redFreq = 0.01;
    greenFreq = 0.013;
    blueFreq = 0.016;
    delta = 25;
    center = 255;
  }
}
