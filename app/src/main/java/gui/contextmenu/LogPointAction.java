package gui.contextmenu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import settings.GlobalSettings;
import settings.Location;
import utils.Utils;

/**
 * Performed when the user selects the log point option.
 */
public class LogPointAction extends AbstractAction {
  private LogPoint logPoint = new LogPoint();
  private JMenuItem logCurrentPoint;
  private Location location;

  public LogPointAction(JMenuItem logCurrentPoint, Location location) {
    this.logCurrentPoint = logCurrentPoint;
    this.location = location;
  }

  @Override
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
    jsonNode.put("Name", logPoint.nameField.getText());
    jsonNode.put("Tags", logPoint.tags.getText());
    jsonNode.put("maxIterations", location.maxIterations);
    jsonNode.put("Re(center)", location.center.re());
    jsonNode.put("Im(center)", location.center.im());
    jsonNode.put("mode", location.mode.toString());
    jsonNode.put("scale", location.scale);

    // read the existing JSON structure and append the new location
    ObjectNode json = Utils.fileToJson(new File(GlobalSettings.pointLogPath));
    json.set(logPoint.nameField.getText(), jsonNode);

    try {
      map.writeValue(new File(GlobalSettings.pointLogPath), json);
    } catch (IOException err) {
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

  /**
   * Represents the menu shown to the user when they select the log point option.
   * Allows a point to be given a name and tags that it can be sorted by
   */
  class LogPoint extends JPanel {
    public JTextField nameField;
    public JTextField tags;

    /**
     * Create a new instance of the menu and set up the fields required.
     */
    public LogPoint() {
      nameField = new JTextField(10);
      tags = new JTextField(5);

      LayoutManager layout = new GridLayout(4, 1);
      setLayout(layout);
      add(new JLabel("Name/Description:"));
      add(nameField);
      add(new JLabel("Tags:"));
      add(tags);

    }
  }
}
