package gui.menu;

import javax.swing.JMenuBar;
import settings.GlobalSettings;

/**
 * Creates a menu bar providing info on the current state of the fractal viewer and allowing
 * the user to change settings manually.
 */
public class MenuBar extends JMenuBar {
  private FileMenu fileMenu;
  private ToolsMenu toolsMenu;
  private ColorMenu colorMenu;
  private InfoMenu infoMenu;

  /**
   * Instantiates a new menu bar, containing the 3 menus used in the fractals application.

   * @param settings - an object containing settings and values used throughout the application
   */
  public MenuBar(GlobalSettings settings) {
    fileMenu = new FileMenu(settings);
    toolsMenu = new ToolsMenu(settings);
    infoMenu = new InfoMenu(settings);
    colorMenu = new ColorMenu(settings);

    add(fileMenu);
    add(toolsMenu);
    add(infoMenu);
    add(colorMenu);
  }

  public void refreshMenu() {
    infoMenu.refresh();
    this.revalidate();
  }
}