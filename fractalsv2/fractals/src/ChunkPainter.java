import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Used to paint a given chunk to an image which can then be drawn onto the viewport.
 */
public class ChunkPainter implements Runnable {
  Chunk chunk;
  BufferedImage image;
  Graphics2D graphics;
  int maxIterations;
  int x;
  int y;

  public ChunkPainter(Chunk chunk, BufferedImage image, int maxIterations, int x, int y) {
    this.chunk = chunk;
    this.maxIterations = maxIterations;
    this.x = x;
    this.y = y;
    this.image = image;
  
    graphics = image.createGraphics();
  }

  @Override
  public void run() {
    for (int x = 0; x < chunk.size; x++) {
      for (int y = 0; y < chunk.size; y++) {
        int iterations = chunk.iterationData[y][x];
        if (iterations == maxIterations || iterations < 0.001 * maxIterations) {
          graphics.setColor(Color.BLACK);
        } else {
          int colour = (iterations + 20) % 255;
          graphics.setColor(new Color(colour, colour, colour));
        }
        graphics.fillRect(x, y, 1, 1);
      }
    }
  }
}
