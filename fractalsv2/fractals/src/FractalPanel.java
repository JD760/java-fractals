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
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Custom class extending the JPanel to create the UI for the fractal viewer.
 */
public class FractalPanel extends JPanel {
  int width;
  int height;
  int colourOffset = 0;
  int maxIterations = 1000;
  Complex center;
  Fractals mode = Fractals.MANDELBROT;
  Complex seed = new Complex(-0.835, 0.232);
  double scale;
  boolean repaintCenter = false;
  int[] centerCoords = new int[] {0, 0};
  Chunk[][] chunks;
  /**
   * Create a new UI window with provided width and height. The window takes the shape
   * of the largest square that can be created on the user's display.

   * @param width - the window width, must be at least 100
   * @param height - the window height, must be at least 100
   */
  public FractalPanel(int width, int height) {
    this.width = width;
    this.height = height;
    this.center = new Complex();
    this.scale = 1;

    InputMap imap = getInputMap();
    ActionMap amap = getActionMap();

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
    imap.put(KeyStroke.getKeyStroke("B"), "changeColour");
    amap.put("changeColour", changeColour);
    imap.put(KeyStroke.getKeyStroke("I"), "increaseIterations");
    amap.put("increaseIterations", increaseIterations);
    imap.put(KeyStroke.getKeyStroke("M"), "nextMode");
    amap.put("nextMode", nextMode);
    imap.put(KeyStroke.getKeyStroke("N"), "prevMode");
    amap.put("prevMode", prevMode);
    addMouseListener(mouseListener);
  }

  MouseAdapter mouseListener = new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
      // we only want click-to-zoom to work within the bounds of the image panel
      if (e.getX() > width || e.getY() > height) {
        return;
      }
      // get points in the complex plane and set the center point to the
      // position of the mouse
      center.setRe(center.re() - ((((e.getX() / (double) width) * 3) / scale) - (2 / scale)));
      center.setIm(center.im() - ((((e.getY() / (double) height) * 3) / scale) - (1.25 / scale)));
      scale *= 2;
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
        ChunkPainter painter = new ChunkPainter(chunks[y][x], image, maxIterations, 32 * x, 32 * y);
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
        chunks[y][x] = new Chunk(32, mode, new int[] {32 * x, 32 * y},
         width, height, center, seed, maxIterations, scale);
        threadpool.execute(chunks[y][x]);
      }
    }

    threadpool.close();

    System.out.println("Iteration Time: " + ((System.nanoTime() - startTime) / 1000000.0) + "ms");
  }

  Action changeColour = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      colourOffset = (int) (255 * Math.random());
      repaint();
      System.out.println("Changed Colour - Offset: " + colourOffset);
    }
  };

  Action moveUp = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center.setIm(center.im() + (0.1 / scale));
      repaint();
    }
  };

  Action moveDown = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center.setIm(center.im() - (0.1 / scale));
      repaint();
    }
  };

  Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center.setRe(center.re() - (0.1 / scale));
      repaint();
    }
  };

  Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center.setRe(center.re() +  (0.1 / scale));
      repaint();
    }
  };

  Action zoomIn = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      scale *= 2.0;
      repaint();
      System.out.println("Scale: " + scale);
    }
  };

  Action zoomOut = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      if (scale > 0.1) {
        scale /= 2.0;
        repaint();
      }
      System.out.println("Scale: " + scale);
    }
  };

  Action resetZoom = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      scale = 1;
      center = new Complex();
      maxIterations = 1000;
      repaint();
    }
  };

  Action repaintCanvas = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      repaint();
    }
  };

  Action increaseIterations = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      maxIterations *= 2;
      repaint();
    }
  };

  Action nextMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (mode.ordinal() + 1) % values.length;
      mode = values[index];
      repaint();
    }
  };

  Action prevMode = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      Fractals[] values = Fractals.values();
      int index = (mode.ordinal() - 1) % values.length;
      mode = values[index];
      repaint();
    }
  };
}