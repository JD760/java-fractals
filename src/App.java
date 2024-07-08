import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gui.FractalPanel;
import gui.Frame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
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
    try {
      new File("src/config/pointLog.json").createNewFile();
      ObjectMapper map = new ObjectMapper();
      ObjectNode jsonNode = map.createObjectNode();
      jsonNode.put("points", "");
      map.writeValue(new File("src/config/pointLog.json"), jsonNode);
    } catch (IOException e) {
      e.printStackTrace();
      return;
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
    f.setSize(width, height);

    //f.add(panel);
    //f.setVisible(true);
  }
}

