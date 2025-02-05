package gui;

import gui.menu.MenuBar;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import settings.GlobalSettings;

/**
 * Create a custom JFrame allowing the behaviour of the main window object to be customised.
 */
public class Frame extends JFrame implements ComponentListener {
  private int prevWidth;
  private int prevHeight;
  private GlobalSettings settings;
  private FractalPanel panel;
  private MenuBar menuBar;

  /**
   * Create a new custom frame, and link the panel corresponding to the fractal viewport.

   * @param panel - A JPanel object on which the fractal will be drawn
   */
  public Frame(FractalPanel panel, GlobalSettings settings) {
    super("Java Fractals");
    this.settings = settings;

    menuBar = new MenuBar(settings);
    setJMenuBar(menuBar);
    settings.menu = menuBar;

    getContentPane().addComponentListener(this);
    prevWidth = this.getWidth();
    prevHeight = this.getHeight();
    this.panel = panel;
    add(panel);
    setVisible(true);
  }

  public void componentShown(ComponentEvent e) {
    return;
  }

  public void componentHidden(ComponentEvent e) {
    return;
  }

  public void componentMoved(ComponentEvent e) {
    return;
  }

  @Override
  public void componentResized(ComponentEvent e) {
    // prevent duplicate events from causing unnecessary redrawing
    if (this.getWidth() != prevWidth || this.getHeight() != prevHeight) {
      this.prevHeight = this.getHeight();
      this.prevWidth = this.getWidth();
      settings.width = getWidth();
      settings.height = getHeight();
      panel.repaint();
    }
  }
}
