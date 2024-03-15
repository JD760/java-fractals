package old.multithreaded;

import java.security.Guard;
import java.security.InvalidParameterException;
import java.util.concurrent.ArrayBlockingQueue;

import old.multithreaded.fractals.Fractal;
import old.multithreaded.gui.FractalCanvas;
import old.multithreaded.gui.GuiFrame;

public class FractalApp {
  static int regionSize = Util.REGION_SIZE;
  static int height = Util.HEIGHT;
  static int width = Util.WIDTH;
  public static void main(String[] args) {
    if (height % regionSize != 0 || width % regionSize != 0) {
      throw new InvalidParameterException("Image dimensions must be divisible"
        + " by region size");
    }

    Fractal myFractal = new Fractal();
    ArrayBlockingQueue<Region> workQueue = myFractal.createRegions();

    FractalCanvas canvas = new FractalCanvas(width, height, workQueue);
    GuiFrame gui = new GuiFrame(width, height, canvas);
  }
}
