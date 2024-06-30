package gui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import settings.GlobalSettings;

/**
 * Creates a menu bar providing info on the current state of the fractal viewer and allowing
 * the user to change settings manually.
 */
public class MenuBar extends JMenuBar {
  private JMenu fileMenu;
  private JMenu modeMenu;
  private InfoMenu infoMenu;

  /**
   * Instantiates a new menu bar, containing the 3 menus used in the fractals application.

   * @param settings - an object containing settings and values used throughout the application
   */
  public MenuBar(GlobalSettings settings) {
    fileMenu = new FileMenu(settings);
    modeMenu = new JMenu("Mode");
    infoMenu = new InfoMenu(settings);

    add(fileMenu);
    add(modeMenu);
    add(infoMenu);
  }

  public void refreshMenu() {
    infoMenu.refresh();
    this.revalidate();
  }
}