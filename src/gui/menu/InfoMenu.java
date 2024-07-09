package gui.menu;

import gui.Location;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import settings.Fractals;
import settings.GlobalSettings;

/**
 * Represents the Info section of the menu bar and the associated functionality
 * including the settings import/export system.
 */
public class InfoMenu extends JMenu {
  private GlobalSettings settings;
  private Location location;
  private JMenuItem centerLabel = new JMenuItem();
  private JMenuItem scaleLabel = new JMenuItem();
  private JMenuItem maxIterationsLabel = new JMenuItem();
  private JMenuItem fractalTypeLabel = new JMenuItem();
  private JMenuItem copySettings = new JMenuItem("Copy Settings");
  private JTextField importSettings = new JTextField();
  private JMenuItem seedLabel = new JMenuItem();

  /**
   * Create a new instance of the info menu, and sets up the associated labels/items.

   * @param settings - the global values and settings for the whole project
   */
  public InfoMenu(GlobalSettings settings) {
    super("Info");
    this.settings = settings;
    this.location = settings.location;
    add(centerLabel);
    add(scaleLabel);
    add(maxIterationsLabel);
    add(fractalTypeLabel);
    add(seedLabel);
    addSeparator();
    add(copySettings);
    importSettings.addActionListener(new ImportAction());
    add(importSettings);
  }

  /**
   * Called whenever the canvas needs to be redrawn, recalculates values to be used on labels
   * before redrawing the GUI.
   */
  public void refresh() {
    centerLabel.setText("Center: " + location.center.toString());
    scaleLabel.setText("Scale: " + Double.toString(location.scale));
    maxIterationsLabel.setText("Max Iterations: " + location.maxIterations);
    fractalTypeLabel.setText("Mode: " + Fractals.toString(location.mode));
    seedLabel.setText("Seed: " + location.seed.toString());
    copySettings.setAction(new CopyAction());
    copySettings.setText("Copy Settings");
    seedLabel.setVisible(
        location.mode == Fractals.JULIA || location.mode == Fractals.JULIA_DIVERGENCE);
  }

  class CopyAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      StringBuilder settingsStr = new StringBuilder("{");
      settingsStr.append(location.center.re() + ",");
      settingsStr.append(location.center.im() + ",");
      settingsStr.append(location.scale + ",");
      settingsStr.append(location.maxIterations + ",");
      settingsStr.append(location.mode.toString() + "}");

      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(new StringSelection(settingsStr.toString()), null);
    }
  }
  
  class ImportAction extends AbstractAction {
    String text = "test";

    /**
     * Attempt to parse the settings string and apply the relevant changes if the string is valid
     * otherwise stop the parsing and inform the user of the parsing issue.
     */
    public void actionPerformed(ActionEvent e) {
      JTextField source = (JTextField) e.getSource();
      String text = source.getText();
      if (text.charAt(0) != '{' || text.charAt(text.length() - 1) != '}') {
        invalidSettings(source, "Invalid format...");
        return;
      }
      text = text.substring(1, text.length());
      String[] settingsStr = text.split(",");
      if (settingsStr.length != 5) {
        invalidSettings(source, "Invalid number of params...");
        return;
      }

      try {
        location.center.setRe(Double.parseDouble(settingsStr[0]));
        location.center.setIm(Double.parseDouble(settingsStr[1]));
        location.scale = Double.parseDouble(settingsStr[2]);
        location.maxIterations = Integer.parseInt(settingsStr[3]);
      } catch (NumberFormatException nfe) {
        System.out.println(nfe);
        invalidSettings(source, "Invalid number format...");
        return;
      }

      location.mode = Fractals.getElement(settingsStr[4]);
      if (location.mode == null) {
        location.mode = Fractals.MANDELBROT;
      }

      settings.panel.repaint();
    }

    private void invalidSettings(JTextField source, String message) {
      source.setText(message);
      source.selectAll();
    }
  }
}