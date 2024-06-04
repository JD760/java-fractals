import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;

/**
 * Contains driver code for setting up the UI and running the application.
 */
public class App {
  /**
   * Create a new UI thread.

   * @param args - currently unused
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGui();
      }
    });
  }

  private static void createAndShowGui() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();
    // create the largest square that can fit on the display
    int viewportSize = Math.min(height - (height % 32), width - (width % 32));
    FractalPanel panel = new FractalPanel(viewportSize, viewportSize);
    Frame f = new Frame(panel);
    f.setSize(viewportSize, viewportSize);

    f.add(panel);
    f.setVisible(true);
  }
}