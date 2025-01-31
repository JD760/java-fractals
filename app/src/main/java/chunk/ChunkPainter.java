package chunk;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import settings.GlobalSettings;

/**
 * Used to paint a given chunk to an image which can then be drawn onto the viewport.
 */
public class ChunkPainter implements Runnable {
  Chunk chunk;
  public BufferedImage image;
  int maxIterations;
  @SuppressWarnings("MemberName")
  public int x;
  @SuppressWarnings("MemberName")
  public int y;
  private static int maxThreads = Runtime.getRuntime().availableProcessors();

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
  }

  /**
   * Runs the iteration data pipeline before painting all chunks through a parallelised approach
   * that works by drawing to an independent image for each chunk, then stitching the resulting
   * images together, allowing for thread safe drawing.

   * @param g - the graphics object for the panel or image being drawn to
   * @param settings - the settings and values global to the entire project
   */
  public static void paintChunks(int width, int height, Graphics g, GlobalSettings settings) {
    Chunk[][] chunks = Chunk.createChunks(width, height, settings);
    ConcurrentLinkedQueue<ChunkPainter> painters = new ConcurrentLinkedQueue<>();
    ExecutorService threadpool = Executors.newFixedThreadPool(Math.max(4, maxThreads - 2));

    for (int y = 0; y < chunks.length; y++) {
      for (int x = 0; x < chunks[0].length; x++) {
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_3BYTE_BGR);
        ChunkPainter painter = new ChunkPainter(
            chunks[y][x], image, settings.location.maxIterations, 32 * x, 32 * y);
        threadpool.execute(painter);
        painters.add(painter);
      }
    }

    threadpool.shutdown();
    try {
      threadpool.awaitTermination(GlobalSettings.DRAWING_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      System.err.println("Iteration thread interrupted");
    }
    
    while (!painters.isEmpty()) {
      ChunkPainter painter = painters.poll();
      g.drawImage(painter.image, painter.x, painter.y, null);
      if (painter.chunk.skipped) {
        g.setColor(Color.RED);
        g.drawRect(painter.x, painter.y, painter.image.getWidth(), painter.image.getHeight());
        g.setColor(Color.WHITE);
      }
    }
  }

  @Override
  public void run() {
    if (chunk.skipped) {
      return;
    }
    for (int x = 0; x < chunk.size; x++) {
      for (int y = 0; y < chunk.size; y++) {
        Color current = chunk.iterationData[y][x];
        image.setRGB(x, y, current.getRGB());
      }
    }
  }
}
