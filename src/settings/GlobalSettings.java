package settings;

import gui.FractalPanel;
import gui.menu.MenuBar;
import java.util.Properties;

/**
 * Contains settings global to the entire project, needs to be top level to allow
 * menu bars to access and change settings.
 */
public class GlobalSettings {
  public int width;
  public int height;
  public int mouseX;
  public int mouseY;
  public Location location;
  public FractalPanel panel;
  public MenuBar menu;

  /**
   * Creates a new global settings object with default values.
   */
  public GlobalSettings() {
    location = new Location();
  }

  /**
   * Creates a new global settings object from the user properties file.

   * @param properties - persistent properties for the whole project.
   */
  public GlobalSettings(Properties properties) {
    this();
  }
}
