package menu;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
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

    public InfoMenu(String text) {
      super(text);
      add(centerLabel);
      add(scaleLabel);
      add(maxIterationsLabel);
      add(fractalTypeLabel);
      addSeparator();
      add(copySettings);
    }

    public void refresh() {
      centerLabel.setText("Center: " + settings.center.toString());
      scaleLabel.setText("Scale: " + Double.toString(settings.scale));
      maxIterationsLabel.setText("Max Iterations: " + settings.maxIterations);
      fractalTypeLabel.setText("Mode: " + settings.mode.toString());
      copySettings.setAction(new CopyAction());
      copySettings.setText("Copy Settings");
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
  }

  /**
   * Instantiates a new menu bar, containing the 3 menus used in the fractals application.

   * @param settings - an object containing settings and values used throughout the application
   */
  public MenuBar(GlobalSettings settings) {
    this.settings = settings;

    fileMenu = new JMenu("File");
    modeMenu = new JMenu("Mode");
    infoMenu = new InfoMenu("Info");

    JMenu infoSubmenu = new JMenu("Import");
    infoSubmenu.add(new JTextArea());
    infoSubmenu.add(new JMenuItem("Set"));
    //infoMenu.add(infoSubmenu);


    add(fileMenu);
    add(modeMenu);
    add(infoMenu);
  }

  public void refreshMenu() {
    infoMenu.refresh();
    this.revalidate();
  }
}