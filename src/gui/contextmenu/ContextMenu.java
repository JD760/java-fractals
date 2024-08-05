package gui.contextmenu;

import gui.interestingpoints.InterestingPoints;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import settings.GlobalSettings;
import settings.Location;

/**
 * The context menu appears when right clicking anywhere on the canvas, and provides
 * quick access to some common options.
 */
public class ContextMenu extends JPopupMenu {
  public Location location;
  JMenuItem showOrbit;
  JMenuItem logCurrentPoint;
  JMenuItem openPoi;

  /**
   * Create a new instance of the context menu.

   * @param settings - A collection of settings and object references for the whole application.
   */
  public ContextMenu(GlobalSettings settings) {
    this.location = settings.location;

    showOrbit = new JMenuItem("Show orbit");
    logCurrentPoint = new JMenuItem("Log Point of Interest");
    openPoi = new JMenuItem("Open Points of Interest");

    openPoi.addActionListener(new OpenAction());
    logCurrentPoint.addActionListener(new LogPointAction(logCurrentPoint, location));
    showOrbit.addActionListener(new ShowOrbitAction(settings, getX(), getY()));
    add(showOrbit);
    addSeparator();
    add(logCurrentPoint);
    add(openPoi);
  }

  class OpenAction extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(
          openPoi,
          new InterestingPoints(location),
          "test",
          JOptionPane.PLAIN_MESSAGE
      );
    }
  }
}
