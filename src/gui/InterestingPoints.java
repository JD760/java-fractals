package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import settings.GlobalSettings;

public class InterestingPoints extends JPanel {
  private JLabel testLabel = new JLabel("Hello World!");

  public InterestingPoints(GlobalSettings settings) {
    add(testLabel);
  }
}
