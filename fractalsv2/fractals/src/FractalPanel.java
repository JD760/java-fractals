import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;
import java.awt.event.*;

public class FractalPanel extends JPanel {
  int width;
  int height;
  int colourOffset = 0;
  double[] center;
  double scale;
  Chunk[][] chunks;

  public FractalPanel(int width, int height) {
    this.width = width;
    this.height = height;
    this.center = new double[] {0, 0};
    this.scale = 1;

    getInputMap().put(KeyStroke.getKeyStroke("UP"), "moveUp");
    getActionMap().put("moveUp", moveUp);
    getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
    getActionMap().put("moveDown", moveDown);
    getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
    getActionMap().put("moveLeft", moveLeft);
    getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
    getActionMap().put("moveRight", moveRight);
    getInputMap().put(KeyStroke.getKeyStroke("Z"), "zoomIn");
    getActionMap().put("zoomIn", zoomIn);
    getInputMap().put(KeyStroke.getKeyStroke("X"), "zoomOut");
    getActionMap().put("zoomOut", zoomOut);
    getInputMap().put(KeyStroke.getKeyStroke("C"), "resetZoom");
    getActionMap().put("resetZoom", resetZoom);
    //getInputMap().put(KeyStroke.getKeyStroke("V"), "autoZoom");
    //getActionMap().put("autoZoom", autoZoom);
    addMouseListener(mouseListener);
  }

  MouseAdapter mouseListener = new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
      // get points in the complex plane and set the center point to the
      // position of the mouse
      center[0] = (((e.getX() / (double) width) * 3) / scale) - (2 / scale);
      center[1] = -((((e.getY() / (double) height) * 3) / scale) - (1.5 / scale));
      System.out.println(Arrays.toString(center));
    }
  };
  

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    long startTime = System.nanoTime();
    createChunks();
    setBackground(Color.BLACK);

    for (int y = 0; y < chunks.length; y++) {
      for (int x = 0; x < chunks[0].length; x++) {
        paintChunk(chunks[y][x], g);
      }
    }
    System.out.println("Painting Time: " + ((System.nanoTime() - startTime) / 1000000.0) + "ms");
  }

  public void paintChunk(Chunk chunk, Graphics g) {
    int startX = chunk.position[0];
    int startY = chunk.position[1];

    for (int y = startY; y < startY + 32; y++) {
      for (int x = startX; x < startX + 32; x++) {
        int iterations = chunk.iterationData[y - startY][x - startX];
        if (iterations == 1000) {
          g.setColor(getBackground());
        } else {
          g.setColor(new Color(0, (iterations + 20) % 255, 0));
        }
        g.fillRect(x, y, 1, 1);
      }
    }
  }

  public void createChunks() {
    int chunksX = width / 32;
    int chunksY = height / 32;
    long startTime = System.nanoTime();
  
    chunks = new Chunk[chunksY][chunksX];
    ExecutorService threadpool = Executors.newFixedThreadPool(10);

    for (int y = 0; y < chunksY; y++) {
      for (int x = 0; x < chunksX; x++) {
        chunks[y][x] = new Chunk(32, new int[] {32 * x, 32 * y}, width, height, center, scale);
        //chunks[y][x].run();
        threadpool.execute(chunks[y][x]);
      }
    }
    threadpool.shutdown();

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
      center[1] += 0.1 / scale;
      repaint();
    }
  };

  Action moveDown = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center[1] -= 0.1 / scale;
      repaint();
    }
  };

  Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center[0] += 0.1 / scale;
      repaint();
      System.out.println(Arrays.toString(center));
    }
  };

  Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      center[0] -= 0.1 / scale;
      repaint();
    }
  };

  Action zoomIn = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      scale *= 2;
      repaint();
    }
  };

  Action zoomOut = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      if (scale > 0.1) {
        scale /= 2;
        repaint();
      }
    }
  };

  Action resetZoom = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      scale = 1;
      center[0] = 0;
      center[1] = 0;
      repaint();
    }
  };
}