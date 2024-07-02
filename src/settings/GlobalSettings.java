package settings;

import complex.Complex;
import gui.FractalPanel;
import gui.menu.MenuBar;

/**
 * Contains settings global to the entire project, needs to be top level to allow
 * menu bars to access and change settings.
 */
public class GlobalSettings {
  public int width;
  public int height;
  public int maxIterations = 1000;
  public Complex center = new Complex();
  public Fractals mode = Fractals.MANDELBROT;
  public Complex seed = new Complex(-0.835, 0.232);
  public double scale = 1.0;
  public FractalPanel panel;
  public MenuBar menu;
}
