package gui;

import complex.Complex;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import settings.Fractals;
import settings.GlobalSettings;

/**
 * Contains all keybinds and associated actions for the project.
 */
public class Actions {
  GlobalSettings settings;

  /**
   * Add all keybinds and actions to the input/action map so they can be registered to the panel.

   * @param settings - an object containing the global settings for the project
   */
  public Actions(GlobalSettings settings) {
    this.settings = settings;
  }

  public Action moveUp = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setIm(center.im() + (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  public Action moveDown = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setIm(center.im() - (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  public Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setRe(center.re() - (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  public Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setRe(center.re() +  (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  public Action zoomIn = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      settings.scale *= 2.0;
      settings.panel.repaint();
      System.out.println("Scale: " + settings.scale);
    }
  };

  public Action zoomOut = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      if (settings.scale > 0.1) {
        settings.scale /= 2.0;
        settings.panel.repaint();
      }
      System.out.println("Scale: " + settings.scale);
    }
  };

  public Action resetZoom = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      settings.scale = 1;
      settings.center = new Complex();
      settings.maxIterations = 1000;
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
      settings.maxIterations *= 2;
      settings.panel.repaint();
    }
  };

  public Action nextMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (settings.mode.ordinal() + 1) % values.length;
      settings.mode = values[index];
      settings.panel.repaint();
    }
  };

  public Action prevMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (settings.mode.ordinal() - 1) % values.length;
      settings.mode = values[index];
      settings.panel.repaint();
    }
  };
}
