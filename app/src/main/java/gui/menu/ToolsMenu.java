package gui.menu;

import gui.Actions;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import settings.GlobalSettings;

/**
 * Contains all the actions the user may take as described
 * in menu.Actions, provides an alternative to keybinds.
 */
public class ToolsMenu extends JMenu {
  private JMenuItem zoomIn = new JMenuItem();
  private JMenuItem zoomOut = new JMenuItem();
  private JMenuItem moveLeft = new JMenuItem();
  private JMenuItem moveUp = new JMenuItem();
  private JMenuItem moveRight = new JMenuItem();
  private JMenuItem moveDown = new JMenuItem();
  private JMenuItem reset = new JMenuItem();
  private JMenuItem redraw = new JMenuItem();
  private JMenuItem increaseIterations = new JMenuItem();
  private JMenuItem nextMode = new JMenuItem();
  private JMenuItem prevMode = new JMenuItem();

  /**
   * Create a new instance of the tools menu and add menu items/actions.

   * @param settings - global settings and values for the whole project.
   */
  public ToolsMenu(GlobalSettings settings) {
    super("Tools");
    final Actions actions = new Actions(settings);

    //zoomOut.setAction(actions.zoomOut);
    add(zoomIn);
    add(zoomOut);
    addSeparator();
    add(moveLeft);
    add(moveUp);
    add(moveRight);
    add(moveDown);
    addSeparator();
    add(increaseIterations);
    addSeparator();
    add(nextMode);
    add(prevMode);
    addSeparator();
    add(reset);
    add(redraw);

    newTool(zoomIn, "Zoom In", actions.zoomIn, "Z");
    newTool(zoomOut, "Zoom Out", actions.zoomOut, "X");
    newTool(moveLeft, "Move Left", actions.moveLeft, "LEFT");
    newTool(moveUp, "Move UP", actions.moveUp, "UP");
    newTool(moveRight, "Move Right", actions.moveRight, "RIGHT");
    newTool(moveDown, "Move Down", actions.moveDown, "DOWN");
    newTool(increaseIterations, "Increase Iterations", actions.increaseIterations, "I");
    newTool(nextMode, "Next Fractal Mode", actions.nextMode, "M");
    newTool(prevMode, "Previous Fractal Mode", actions.prevMode, "N");
    newTool(reset, "Reset", actions.resetZoom, "C");
    newTool(redraw, "Redraw Canvas", actions.repaintCanvas, "V");
  }

  private void newTool(JMenuItem item, String text, Action action, String keyStroke) {
    item.setAction(action);
    item.setText(text);
    item.setAccelerator(KeyStroke.getKeyStroke(keyStroke));
  }
}
