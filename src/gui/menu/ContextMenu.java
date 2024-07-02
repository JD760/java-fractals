package gui.menu;

import gui.InterestingPoints;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import settings.GlobalSettings;

/**
 * The context menu appears when right clicking anywhere on the canvas, and provides
 * quick access to some common options.
 */
public class ContextMenu extends JPopupMenu {
  private GlobalSettings settings;
  JMenuItem showOrbit;
  JMenuItem logCurrentPoint;
  JMenuItem openPoi;

  /**
   * Create a new instance of the context menu.

   * @param settings - global settings and values to the whole project
   */
  public ContextMenu(GlobalSettings settings) {
    this.settings = settings;

    showOrbit = new JMenuItem("Show orbit");
    logCurrentPoint = new JMenuItem("Log Point of Interest");
    openPoi = new JMenuItem("Open Points of Interest");

    openPoi.addActionListener(new OpenAction());
    add(showOrbit);
    addSeparator();
    add(logCurrentPoint);
    add(openPoi);
  }

  class OpenAction extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(
          logCurrentPoint,
          new InterestingPoints(settings),
          "test",
          JOptionPane.PLAIN_MESSAGE
      );
    }
  }
}
