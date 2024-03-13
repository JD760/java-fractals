package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the canvas used to draw the fractal pixels.
 */
public class FractalCanvas extends Canvas {
  public FractalCanvas() {
    setBackground(Color.CYAN);
    setSize(getWidth(), getHeight());
  }

  public void paint(Graphics g) {
    g.setColor(Color.RED);
    g.drawRect(10, 10, 1, 1);
  }
}
