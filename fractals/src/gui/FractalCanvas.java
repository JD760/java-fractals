package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the canvas used to draw the fractal pixels.
 */
public class FractalCanvas extends Canvas {
  private int[][][] pixelData;
  private int width;
  private int height;

  /**
   * Creates a new fractalCanvas, used to display the coloured pixels of the fractal.

   * @param width - width of the image
   * @param height - height of the image
   * @param pixelData - width*height array of pixels
   */
  public FractalCanvas(int width, int height, int[][][] pixelData) {
    this.pixelData = pixelData;
    this.width = width;
    this.height = height;
    setBackground(Color.WHITE);
    setSize(getWidth(), getHeight());
  }

  /**
   * Draw the pixels onto the canvas and output the resulting image to the screen.
   */
  public void paint(Graphics g) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int[] pixel = pixelData[y][x];
        g.setColor(new Color(pixel[0], pixel[1], pixel[2]));
        g.drawRect(x, y, 1, 1);
      }
    }
  }
}
