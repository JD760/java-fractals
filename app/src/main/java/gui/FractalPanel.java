package gui;

import chunk.Chunk;
import chunk.ChunkPainter;
import complex.Complex;
import complex.Point;
import gui.contextmenu.ContextMenu;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import settings.GlobalSettings;

/**
 * Custom class extending the JPanel to create the UI for the fractal viewer.
 */
public class FractalPanel extends JPanel {
  GlobalSettings settings = new GlobalSettings();
  int width;
  int height;
  boolean repaintCenter = false;
  Chunk[][] chunks;

  /**
   * Create a new UI window with provided width and height. The window takes the shape
   * of the largest square that can be created on the user's display.
   */
  public FractalPanel(GlobalSettings settings) {
    // optimise the drawing process slightly by telling swing we will always paint
    // all of the pixels and never require transparency.
    setOpaque(true);
    // we do not need double buffering as we have no smooth transitions
    // due to the chaotic nature of most fractals
    setDoubleBuffered(false);
    this.width = settings.width;
    this.height = settings.height;
    this.settings = settings;
    settings.panel = this;
    new Actions(settings);
    addMouseListener(new ClickListener(settings));
  }

  class ClickListener extends MouseAdapter {
    private ContextMenu menu;

    public ClickListener(GlobalSettings settings) {
      menu = new ContextMenu(settings);
    }

    @Override
    public void mousePressed(MouseEvent e) {
      settings.mouseCoords = new Point(e.getX(), e.getY());

      if (e.isPopupTrigger()) {
        menu.show(e.getComponent(), e.getX(), e.getY());
        return;
      }

      settings.centerCoords = new Point(e.getX(), e.getY());
      Complex center = settings.location.center;
      double scale = settings.location.scale;
      // we only want click-to-zoom to work within the bounds of the image panel
      if (e.getX() > width || e.getY() > height) {
        return;
      }
      // get points in the complex plane and set the center point to the
      // position of the mouse
      center.setRe(center.re() - (1 / scale) * ((3 * e.getX() / (double) width) - 2));
      center.setIm(center.im() - (1 / scale) * ((3 * e.getY() / (double) height) - 1.25));
      settings.location.scale *= 2;
      repaintCenter = true;
      repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (e.isPopupTrigger()) {
        menu.show(e.getComponent(), e.getX(), e.getY());
        return;
      }
    }
  }
  

  @Override
  public void paintComponent(Graphics g) {
    ChunkPainter.paintChunks(settings.width, settings.height, g, settings);
    if (repaintCenter) {
      g.setColor(Color.RED);
      g.fillRect(settings.centerCoords.x - 1, settings.centerCoords.y - 1, 3, 3);
      repaintCenter = false;
    }
    settings.menu.refreshMenu();
  }
}