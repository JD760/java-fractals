package settings;

import complex.Point;
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
  public Point centerCoords;
  public Point mouseCoords;
  public Location location;
  public ColorSettings colorSettings;
  public FractalPanel panel;
  public MenuBar menu;

  public static final String POINT_LOG_PATH = "src/main/java/config/pointLog.json";
  public static final int DRAWING_TIMEOUT_MILLIS = 10_000;
  public static boolean DEBUG = false;

  /**
   * Creates a new global settings object with default values.
   */
  public GlobalSettings() {
    location = new Location();
    colorSettings = new ColorSettings();
    centerCoords = new Point();
    mouseCoords = new Point();
  }

  /**
   * Create a Settings object in the format for an icon as used in the Interesting Points
   * feature.

   * @param width - the width of the icon in pixels
   * @param height - the height of the icon in pixels
   * @param location - A Location object containing the information needed to render the image
   */
  public GlobalSettings(int width, int height, Location location) {
    this();
    this.width = width;
    this.height = height;
    this.location = location;
  }

  /**
   * Creates a new global settings object from the user properties file.

   * @param properties - persistent properties for the whole project.
   */
  public GlobalSettings(Properties properties) {
    this();
  }
}
