package settings;

import complex.Complex;
import gui.FractalPanel;
import gui.menu.MenuBar;
import java.util.Properties;

/**
 * Contains settings global to the entire project, needs to be top level to allow
 * menu bars to access and change settings.
 */
public class GlobalSettings {
  public int width;
  public int height;
  public int maxIterations;
  public Complex center;
  public Fractals mode;
  public Complex seed;
  public double scale;
  public FractalPanel panel;
  public MenuBar menu;

  /**
   * Creates a new global settings object with default values.
   */
  public GlobalSettings() {
    maxIterations = 1000;
    center = new Complex();
    mode = Fractals.MANDELBROT;
    seed = new Complex(-0.835, 0.232);
    scale = 1.0;
  }

  /**
   * Creates a new global settings object from the user properties file.

   * @param properties - persistent properties for the whole project.
   */
  public GlobalSettings(Properties properties) {
    this();
  }
}
