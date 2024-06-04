import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
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
  double[] center;
  double scale;
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
    this.center = new double[] {0, 0};
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
    //getInputMap().put(KeyStroke.getKeyStroke("V"), "autoZoom");
    //getActionMap().put("autoZoom", autoZoom);
    addMouseListener(mouseListener);
  }

  MouseAdapter mouseListener = new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
      // get points in the complex plane and set the center point to the
      // position of the mouse
      center[0] = center[0] - ((((e.getX() / (double) width) * 3) / scale) - (2 / scale));
      center[1] = center[1] - ((((e.getY() / (double) height) * 3) / scale) - (1.25 / scale));
      scale *= 2;
      repaint();
      System.out.println(Arrays.toString(center));
    }
  };
  

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    createChunks();
    long startTime = System.nanoTime();

    for (int y = 0; y < chunks.length; y++) {
      for (int x = 0; x < chunks[0].length; x++) {
        paintChunk(chunks[y][x], g);
      }
    }
    System.out.println("Painting Time: " + ((System.nanoTime() - startTime) / 1000000.0) + "ms");
  }

  /**
   * Use the iteration data contained in each chunk to paint the fractal image with
   * pixel data derived from the chunk data.

   * @param chunk - represents a region in the complex plane
   * @param g - graphics object used to paint to the panel canvas
   */
  public void paintChunk(Chunk chunk, Graphics g) {
    int startX = chunk.position[0];
    int startY = chunk.position[1];

    for (int y = 0; y < 32; y++) {
      for (int x = 0; x < 32; x++) {
        int iterations = chunk.iterationData[y][x];
        if (iterations == maxIterations) {
          g.setColor(new Color(0));
        } else if (iterations < 0.001 * maxIterations) {
          g.setColor(new Color(0));
        } else {
          int colour = (iterations + 20 + (colourOffset % 120)) % 255;
          g.setColor(new Color(colour, colour, colour));
        }
        g.fillRect(startX + x, startY + y, 1, 1);
      }
    }
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
        chunks[y][x] = new Chunk(
          32, new int[] {32 * x, 32 * y}, width, height, center, maxIterations, scale);
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
      center[1] -= 0.1 / scale;
      repaint();
      System.out.println(Arrays.toString(center));
    }
  };

  Action moveDown = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center[1] += 0.1 / scale;
      repaint();
      System.out.println(Arrays.toString(center));
    }
  };

  Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center[0] -= 0.1 / scale;
      repaint();
      System.out.println(Arrays.toString(center));
    }
  };

  Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center[0] += 0.1 / scale;
      repaint();
      System.out.println(Arrays.toString(center));
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
      center[0] = 0;
      center[1] = 0;
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
}