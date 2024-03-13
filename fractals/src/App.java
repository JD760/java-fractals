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
    FractalCanvas canvas = new FractalCanvas();
    @SuppressWarnings("unused")
    Gui gui = new Gui(WIDTH, HEIGHT, canvas);
  }
}
