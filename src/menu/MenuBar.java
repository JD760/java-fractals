package menu;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import settings.Fractals;
import settings.GlobalSettings;

/**
 * Creates a menu bar providing info on the current state of the fractal viewer and allowing
 * the user to change settings manually.
 */
public class MenuBar extends JMenuBar {
  private JMenu fileMenu;
  private JMenu modeMenu;
  private InfoMenu infoMenu;
  private GlobalSettings settings;

  class InfoMenu extends JMenu {
    private JMenuItem centerLabel = new JMenuItem();
    private JMenuItem scaleLabel = new JMenuItem();
    private JMenuItem maxIterationsLabel = new JMenuItem();
    private JMenuItem fractalTypeLabel = new JMenuItem();
    private JMenuItem copySettings = new JMenuItem("Copy Settings");
    private JTextField importSettings = new JTextField();
    private JMenuItem seedLabel = new JMenuItem();

    public InfoMenu(String text) {
      super(text);
      add(centerLabel);
      add(scaleLabel);
      add(maxIterationsLabel);
      add(fractalTypeLabel);
      add(seedLabel);
      addSeparator();
      add(copySettings);
      importSettings.addActionListener(new ImportAction());
      add(importSettings);
    }

    public void refresh() {
      centerLabel.setText("Center: " + settings.center.toString());
      scaleLabel.setText("Scale: " + Double.toString(settings.scale));
      maxIterationsLabel.setText("Max Iterations: " + settings.maxIterations);
      fractalTypeLabel.setText("Mode: " + Fractals.toString(settings.mode));
      seedLabel.setText("Seed: " + settings.seed.toString());
      copySettings.setAction(new CopyAction());
      copySettings.setText("Copy Settings");
      seedLabel.setVisible(
          settings.mode == Fractals.JULIA || settings.mode == Fractals.JULIA_DIVERGENCE);
    }

    class CopyAction extends AbstractAction {
      @Override
      public void actionPerformed(ActionEvent e) {
        StringBuilder settingsStr = new StringBuilder("{");
        settingsStr.append(settings.center.re() + ",");
        settingsStr.append(settings.center.im() + ",");
        settingsStr.append(settings.scale + ",");
        settingsStr.append(settings.maxIterations + ",");
        settingsStr.append(settings.mode.toString() + "}");

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(settingsStr.toString()), null);
      }
    }
    
    class ImportAction extends AbstractAction {
      String text = "test";

      /**
       * Attempt to parse the settings string and apply the relevant changes if the string is valid
       * otherwise stop the parsing and inform the user of the parsing issue.
       */
      public void actionPerformed(ActionEvent e) {
        JTextField source = (JTextField) e.getSource();
        String text = source.getText();
        if (text.charAt(0) != '{' || text.charAt(text.length() - 1) != '}') {
          invalidSettings(source, "Invalid format...");
          return;
        }
        text = text.substring(1, text.length());
        String[] settingsStr = text.split(",");
        if (settingsStr.length != 5) {
          invalidSettings(source, "Invalid number of params...");
          return;
        }

        try {
          settings.center.setRe(Double.parseDouble(settingsStr[0]));
          settings.center.setIm(Double.parseDouble(settingsStr[1]));
          settings.scale = Double.parseDouble(settingsStr[2]);
          settings.maxIterations = Integer.parseInt(settingsStr[3]);
        } catch (NumberFormatException nfe) {
          System.out.println(nfe);
          invalidSettings(source, "Invalid number format...");
          return;
        }

        settings.mode = Fractals.getElement(settingsStr[4]);
        if (settings.mode == null) {
          settings.mode = Fractals.MANDELBROT;
        }

        settings.panel.repaint();
      }

      private void invalidSettings(JTextField source, String message) {
        source.setText(message);
        source.selectAll();
      }
    }
  }

  class FileMenu extends JMenu {
    private JMenuItem saveImage = new JMenuItem("Save Image");

    public FileMenu(String name) {
      super(name);
      add(saveImage);
    }
  }

  /**
   * Instantiates a new menu bar, containing the 3 menus used in the fractals application.

   * @param settings - an object containing settings and values used throughout the application
   */
  public MenuBar(GlobalSettings settings) {
    this.settings = settings;

    fileMenu = new FileMenu("File");
    modeMenu = new JMenu("Mode");
    infoMenu = new InfoMenu("Info");

    add(fileMenu);
    add(modeMenu);
    add(infoMenu);
  }

  public void refreshMenu() {
    infoMenu.refresh();
    this.revalidate();
  }
}