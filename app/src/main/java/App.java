import gui.FractalPanel;
import gui.Frame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import settings.GlobalSettings;
import utils.Utils;

//TODO: Add more types of fractals (burning ship, fix Julia Set code)
//TODO: Add context options to the Interesting Points such as modify/delete
//TODO: Implement different colouring methods

/**
 * Contains driver code for setting up the UI and running the application.
 */
public class App {
  /**
   * Create a new UI thread.

   * @param args - currently unused
   */
  public static void main(String[] args) {
    String jsonContents = Utils.readFile(new File(GlobalSettings.pointLogPath));
    if (jsonContents.length() == 0) {
      try {
        FileWriter writer = new FileWriter(new File(GlobalSettings.pointLogPath));
        writer.write("{}");
        writer.close();
      } catch (IOException e) {
        System.err.println("Error while writing to file");
      }
    }
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
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    f.setSize(width, height);
  }
}

