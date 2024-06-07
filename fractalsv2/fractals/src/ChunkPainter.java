import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Used to paint a given chunk to an image which can then be drawn onto the viewport.
 */
public class ChunkPainter implements Runnable {
  Chunk chunk;
  BufferedImage image;
  Graphics2D graphics;
  int maxIterations;
  @SuppressWarnings("MemberName")
  int x;
  @SuppressWarnings("MemberName")
  int y;

  /**
   * Create a new Chunk Painter, which maps a chunk to an image
   * following the colouring algorithm of choice.

   * @param chunk - the chunk to read iteration data from
   * @param image - an empty image of the same size as the chunk
   * @param maxIterations - the maximum iterations before a point is deemed within the set
   * @param x - the x co-ordinate of the top left corner of the chunk
   * @param y - the y co-ordinate of the top left corner of the chunk
   */
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
        if (iterations == maxIterations) {
          graphics.setColor(Color.BLACK);
        } else {
          int colour = (iterations) % 255;
          graphics.setColor(new Color(colour, colour, colour));
        }
        graphics.fillRect(x, y, 1, 1);
      }
    }
  }
}
