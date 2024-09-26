package gui;

import complex.Complex;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import settings.Fractals;
import settings.GlobalSettings;
import settings.Location;

/**
 * Contains all keybinds and associated actions for the project.
 */
public class Actions {
  GlobalSettings settings;
  Location location;

  /**
   * Add all keybinds and actions to the input/action map so they can be registered to the panel.

   * @param settings - an object containing the global settings for the project
   */
  public Actions(GlobalSettings settings) {
    this.settings = settings;
    this.location = settings.location;
  }

  public Action moveUp = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = location.center;
      center.setIm(center.im() + (0.1 / location.scale));
      settings.panel.repaint();
    }
  };

  public Action moveDown = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = location.center;
      center.setIm(center.im() - (0.1 / location.scale));
      settings.panel.repaint();
    }
  };

  public Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = location.center;
      center.setRe(center.re() - (0.1 / location.scale));
      settings.panel.repaint();
    }
  };

  public Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = location.center;
      center.setRe(center.re() +  (0.1 / location.scale));
      settings.panel.repaint();
    }
  };

  public Action zoomIn = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      location.scale *= 2.0;
      settings.panel.repaint();
    }
  };

  public Action zoomOut = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      if (location.scale > 0.1) {
        location.scale /= 2.0;
        settings.panel.repaint();
      }
    }
  };

  public Action resetZoom = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      location.scale = 1;
      location.center = new Complex();
      location.maxIterations = 1000;
      settings.panel.repaint();
    }
  };

  public Action repaintCanvas = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      settings.panel.repaint();
    }
  };

  public Action increaseIterations = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      location.maxIterations *= 2;
      settings.panel.repaint();
    }
  };

  public Action nextMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (location.mode.ordinal() + 1) % values.length;
      location.mode = values[index];
      settings.panel.repaint();
    }
  };

  public Action prevMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (location.mode.ordinal() - 1) % values.length;
      location.mode = values[index];
      settings.panel.repaint();
    }
  };
}
