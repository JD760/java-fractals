package gui.interestingpoints;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Represents the menu shown to the user when they select the log point option.
 * Allows a point to be given a name and tags that it can be sorted by
 */
public class LogPoint extends JPanel {
  public JTextField nameField;
  private JTextField tags;

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

  @Override
  public String getName() {
    return nameField.getText();
  }
}
