package gui.interestingpoints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import settings.GlobalSettings;

/**
 * Represents the Interesting Points feature, which allows the user to log
 * and sort positions in the fractal that they may return to later.
 */
public class InterestingPoints extends JPanel {
  private JLabel testLabel = new JLabel("Hello World!");

  public InterestingPoints(GlobalSettings settings) {
    add(testLabel);
  }
}
