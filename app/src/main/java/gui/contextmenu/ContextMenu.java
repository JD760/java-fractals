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
  private GlobalSettings settings;
  JMenuItem pointInfo;
  JMenuItem showOrbit;
  JMenuItem logCurrentPoint;
  JMenuItem openPoi;

  /**
   * Create a new instance of the context menu.

   * @param settings - A collection of settings and object references for the whole application.
   */
  public ContextMenu(GlobalSettings settings) {
    this.settings = settings;
    this.location = settings.location;

    pointInfo = new JMenuItem("(");
    showOrbit = new JMenuItem("Show orbit");
    logCurrentPoint = new JMenuItem("Log Point of Interest");
    openPoi = new JMenuItem("Open Points of Interest");

    openPoi.addActionListener(new OpenAction());
    logCurrentPoint.addActionListener(new LogPointAction(logCurrentPoint, location));
    showOrbit.addActionListener(new ShowOrbitAction(settings, getX(), getY()));
    add(pointInfo);
    addSeparator();
    add(showOrbit);
    addSeparator();
    add(logCurrentPoint);
    add(openPoi);
  }

  class OpenAction extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      Location selected = new Location();
      JOptionPane.showMessageDialog(
          settings.panel,
          new InterestingPoints(selected),
          "Points of Interest Log",
          JOptionPane.PLAIN_MESSAGE
      );
      if (selected.equals(new Location())) {
        return;
      }

      settings.location.update(
          selected.center, selected.seed, selected.scale, selected.maxIterations, selected.mode
      );
      settings.panel.repaint();
    }

  }
}
