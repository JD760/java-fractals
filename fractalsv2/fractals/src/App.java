import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;

public class App {
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
    int viewportSize = height - (height % 32);
    FractalPanel panel = new FractalPanel(viewportSize, viewportSize);
    Frame f = new Frame(panel);
    f.setSize(viewportSize, viewportSize);

    f.add(panel);
    f.setVisible(true);
  }
}