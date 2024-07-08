package gui.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gui.interestingpoints.InterestingPoints;
import gui.interestingpoints.LogPoint;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
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
  public GlobalSettings settings;
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
          new InterestingPoints(settings),
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
      jsonNode.put("width", settings.width);
      jsonNode.put("height", settings.height);
      jsonNode.put("maxIterations", settings.maxIterations);
      jsonNode.put("Re(center)", settings.center.re());
      jsonNode.put("Im(center)", settings.center.im());
      jsonNode.put("mode", settings.mode.toString());
      jsonNode.put("scale", settings.scale);
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
