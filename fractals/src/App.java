import java.util.Arrays;

import colouring.Colouring;
import fractals.Fractal;
import gui.FractalCanvas;
import gui.Gui;

/**
 * Main app instance, contains driver code to run the fractals application.
 */
public class App {
  static int WIDTH = 1440;
  static int HEIGHT = 720;

  /**
   * Create a new instance of the application.

   * @param args - application arguments
   */
  public static void main(String[] args) {
    // generate a fractal
    Fractal myFractal = new Fractal(WIDTH, HEIGHT);
    myFractal.mandelbrot();
    int[][] data = myFractal.getIterationData();

    Colouring colouring = new Colouring(WIDTH, HEIGHT, 1000, data);
    int[][][] pixels = colouring.modulusColouring();

    // colour the fractal from its' iteration data
    // create the GUI
    FractalCanvas canvas = new FractalCanvas(WIDTH, HEIGHT, pixels);
    @SuppressWarnings("unused")
    Gui gui = new Gui(WIDTH, HEIGHT, canvas);
  }
}
