import gui.FractalPanel;
import gui.Frame;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import settings.GlobalSettings;

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
    //int viewportSize = Math.min(height - (height % 32), width - (width % 32));
    GlobalSettings settings = new GlobalSettings();
    settings.width = width - (width % 32);
    settings.height = height - (height % 32);

    FractalPanel panel = new FractalPanel(settings);
    Frame f = new Frame(panel, settings);
    f.setSize(width, height);

    //f.add(panel);
    //f.setVisible(true);
  }
}

