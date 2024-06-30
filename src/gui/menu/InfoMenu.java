package gui.menu;

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
    centerLabel.setText("Center: " + settings.center.toString());
    scaleLabel.setText("Scale: " + Double.toString(settings.scale));
    maxIterationsLabel.setText("Max Iterations: " + settings.maxIterations);
    fractalTypeLabel.setText("Mode: " + Fractals.toString(settings.mode));
    seedLabel.setText("Seed: " + settings.seed.toString());
    copySettings.setAction(new CopyAction());
    copySettings.setText("Copy Settings");
    seedLabel.setVisible(
        settings.mode == Fractals.JULIA || settings.mode == Fractals.JULIA_DIVERGENCE);
  }

  class CopyAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      StringBuilder settingsStr = new StringBuilder("{");
      settingsStr.append(settings.center.re() + ",");
      settingsStr.append(settings.center.im() + ",");
      settingsStr.append(settings.scale + ",");
      settingsStr.append(settings.maxIterations + ",");
      settingsStr.append(settings.mode.toString() + "}");

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
        settings.center.setRe(Double.parseDouble(settingsStr[0]));
        settings.center.setIm(Double.parseDouble(settingsStr[1]));
        settings.scale = Double.parseDouble(settingsStr[2]);
        settings.maxIterations = Integer.parseInt(settingsStr[3]);
      } catch (NumberFormatException nfe) {
        System.out.println(nfe);
        invalidSettings(source, "Invalid number format...");
        return;
      }

      settings.mode = Fractals.getElement(settingsStr[4]);
      if (settings.mode == null) {
        settings.mode = Fractals.MANDELBROT;
      }

      settings.panel.repaint();
    }

    private void invalidSettings(JTextField source, String message) {
      source.setText(message);
      source.selectAll();
    }
  }
}