package gui.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gui.Location;
import gui.interestingpoints.InterestingPoints;
import gui.interestingpoints.LogPoint;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

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

   * @param location - Represents a specific point and configuration on the canvas.
   */
  public ContextMenu(Location location) {
    this.location = location;

    showOrbit = new JMenuItem("Show orbit");
    logCurrentPoint = new JMenuItem("Log Point of Interest");
    openPoi = new JMenuItem("Open Points of Interest");

    openPoi.addActionListener(new OpenAction());
    logCurrentPoint.addActionListener(new LogPointAction());
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

  class LogPointAction extends AbstractAction {
    private LogPoint logPoint = new LogPoint();

    public void actionPerformed(ActionEvent e) {
      int result = JOptionPane.showConfirmDialog(
          logCurrentPoint,
          logPoint,
          "Log New Interesting Point",
          JOptionPane.PLAIN_MESSAGE
      );

      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      if (logPoint.nameField.getText().length() == 0) {
        validationError("Name cannot be empty!");
        return;
      }

      ObjectMapper map = new ObjectMapper();
      ObjectNode jsonNode = map.createObjectNode();
      jsonNode.put("maxIterations", location.maxIterations);
      jsonNode.put("Re(center)", location.center.re());
      jsonNode.put("Im(center)", location.center.im());
      jsonNode.put("mode", location.mode.toString());
      jsonNode.put("scale", location.scale);
      try {
        map.writeValue(new File("src/config/pointLog.json"), jsonNode);
      } catch (IOException e1) {
        validationError("Error writing to point log file");
        return;
      }
    }

    private void validationError(String message) {
      JOptionPane.showMessageDialog(
          logCurrentPoint,
          message,
          "Error",
          JOptionPane.WARNING_MESSAGE
      );
    }
  }
}
