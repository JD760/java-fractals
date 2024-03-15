package old.multithreaded.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import old.multithreaded.Region;
import old.multithreaded.Util;

/**
 * Represents the canvas used to draw the fractal pixels.
 */
public class FractalCanvas extends Canvas {
  private ArrayBlockingQueue<Region> workQueue;
  private int width;
  private int height;

  /**
   * Creates a new fractalCanvas, used to display the coloured pixels of the fractal.

   * @param width - width of the image
   * @param height - height of the image
   * @param workQueue - thread-safe queue of regions to render onto the screen
   */
  public FractalCanvas(int width, int height, ArrayBlockingQueue<Region> workQueue) {
    this.workQueue = workQueue;
    this.width = width;
    this.height = height;
    setBackground(Color.WHITE);
    setSize(getWidth(), getHeight());
  }

  /**
   * Draw the pixels onto the canvas and output the resulting image to the screen.
   */
  public void paint(Graphics g) {
    Color[] colors = {Color.BLACK, Color.CYAN, Color.GREEN, Color.PINK};
    Random rand = new Random();
    for (int i = 0; i < workQueue.size(); i++) {
      Region current = workQueue.poll();
      g.setColor(colors[rand.nextInt(colors.length)]);
      System.out.println(current.getX() + " , " + current.getY());
      g.drawRect(current.getX(), current.getY(), Util.REGION_SIZE, Util.REGION_SIZE);
      if (current.getY() == 696) {
        g.setColor(Color.ORANGE);
      }
      g.fillRect(current.getX(), current.getY(), Util.REGION_SIZE, Util.REGION_SIZE);
    }
  }

  public void run() {
    return;
  }
}
