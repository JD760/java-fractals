package gui;

import complex.Complex;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
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
    InputMap imap = settings.panel.getInputMap();
    ActionMap amap = settings.panel.getActionMap();

    imap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
    amap.put("moveUp", moveUp);
    imap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
    amap.put("moveDown", moveDown);
    imap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
    amap.put("moveLeft", moveLeft);
    imap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
    amap.put("moveRight", moveRight);
    imap.put(KeyStroke.getKeyStroke("Z"), "zoomIn");
    amap.put("zoomIn", zoomIn);
    imap.put(KeyStroke.getKeyStroke("X"), "zoomOut");
    amap.put("zoomOut", zoomOut);
    imap.put(KeyStroke.getKeyStroke("C"), "resetZoom");
    amap.put("resetZoom", resetZoom);
    imap.put(KeyStroke.getKeyStroke("V"), "repaintCanvas");
    amap.put("repaintCanvas", repaintCanvas);
    imap.put(KeyStroke.getKeyStroke("I"), "increaseIterations");
    amap.put("increaseIterations", increaseIterations);
    imap.put(KeyStroke.getKeyStroke("M"), "nextMode");
    amap.put("nextMode", nextMode);
    imap.put(KeyStroke.getKeyStroke("N"), "prevMode");
    amap.put("prevMode", prevMode);
  }

  Action moveUp = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setIm(center.im() + (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  Action moveDown = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setIm(center.im() - (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setRe(center.re() - (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setRe(center.re() +  (0.1 / settings.scale));
      settings.panel.repaint();
    }
  };

  Action zoomIn = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      settings.scale *= 2.0;
      settings.panel.repaint();
      System.out.println("Scale: " + settings.scale);
    }
  };

  Action zoomOut = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      if (settings.scale > 0.1) {
        settings.scale /= 2.0;
        settings.panel.repaint();
      }
      System.out.println("Scale: " + settings.scale);
    }
  };

  Action resetZoom = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      settings.scale = 1;
      settings.center = new Complex();
      settings.maxIterations = 1000;
      settings.panel.repaint();
    }
  };

  Action repaintCanvas = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      settings.panel.repaint();
    }
  };

  Action increaseIterations = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      settings.maxIterations *= 2;
      settings.panel.repaint();
    }
  };

  Action nextMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (settings.mode.ordinal() + 1) % values.length;
      settings.mode = values[index];
      settings.panel.repaint();
    }
  };

  Action prevMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (settings.mode.ordinal() - 1) % values.length;
      settings.mode = values[index];
      settings.panel.repaint();
    }
  };
}
