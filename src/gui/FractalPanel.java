package gui;

import chunk.Chunk;
import chunk.ChunkPainter;
import complex.Complex;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import settings.GlobalSettings;

/**
 * Custom class extending the JPanel to create the UI for the fractal viewer.
 */
public class FractalPanel extends JPanel {
  GlobalSettings settings;
  int width;
  int height;
  boolean repaintCenter = false;
  int[] centerCoords = new int[] {0, 0};
  Chunk[][] chunks;

  /**
   * Create a new UI window with provided width and height. The window takes the shape
   * of the largest square that can be created on the user's display.
   */
  public FractalPanel(GlobalSettings settings) {
    this.width = settings.width;
    this.height = settings.height;
    this.settings = settings;
    settings.panel = this;
    new Actions(settings);

    //imap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
    //amap.put("moveUp", moveUp);
    addMouseListener(mouseListener);
  }

  MouseAdapter mouseListener = new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
      Complex center = settings.center;
      double scale = settings.scale;
      // we only want click-to-zoom to work within the bounds of the image panel
      if (e.getX() > width || e.getY() > height) {
        return;
      }
      // get points in the complex plane and set the center point to the
      // position of the mouse
      center.setRe(center.re() - ((((e.getX() / (double) width) * 3) / scale) - (2 / scale)));
      center.setIm(center.im() - ((((e.getY() / (double) height) * 3) / scale) - (1.25 / scale)));
      settings.scale *= 2;
      centerCoords = new int[]{e.getX(), e.getY()};
      System.out.println("Center: " + center.toString());
      repaintCenter = true;
      repaint();
    }
  };
  

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    createChunks();
    final long startTime = System.nanoTime();

    ConcurrentLinkedQueue<ChunkPainter> painters = new ConcurrentLinkedQueue<>();
    ExecutorService threadpool = Executors.newFixedThreadPool(10);

    for (int y = 0; y < chunks.length; y++) {
      for (int x = 0; x < chunks[0].length; x++) {
        //paintChunk(chunks[y][x], g);
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_3BYTE_BGR);
        ChunkPainter painter = new ChunkPainter(
            chunks[y][x], image, settings.maxIterations, 32 * x, 32 * y);
        threadpool.execute(painter);
        painters.add(painter);
      }
    }

    threadpool.close();

    while (!painters.isEmpty()) {
      ChunkPainter painter = painters.poll();
      g.drawImage(painter.image, painter.x, painter.y, this);
    }

    if (repaintCenter) {
      g.setColor(Color.RED);
      g.fillRect(centerCoords[0] - 1, centerCoords[1] - 1, 3, 3);
      repaintCenter = false;
    }
    settings.menu.refreshMenu();
    System.out.println("Painting Time: " + ((System.nanoTime() - startTime) / 1000000.0) + "ms");
    System.out.println("--------------------");
  }

  /**
   * Create the array of chunks, representing pixel data for regions of an image.
   * Using a threadpool, process the pixel data for each region in parallel
   */
  public void createChunks() {
    int chunksX = width / 32;
    int chunksY = height / 32;
    final long startTime = System.nanoTime();
  
    chunks = new Chunk[chunksY][chunksX];
    ExecutorService threadpool = Executors.newFixedThreadPool(10);

    for (int y = 0; y < chunksY; y++) {
      for (int x = 0; x < chunksX; x++) {
        chunks[y][x] = new Chunk(32, new int[] {32 * x, 32 * y}, settings);
        threadpool.execute(chunks[y][x]);
      }
    }

    threadpool.close();

    System.out.println("Iteration Time: " + ((System.nanoTime() - startTime) / 1000000.0) + "ms");
  }

  Action moveUp = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Complex center = settings.center;
      center.setIm(center.im() + (0.1 / settings.scale));
      repaint();
    }
  };
}