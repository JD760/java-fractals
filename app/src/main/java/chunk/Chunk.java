package chunk;

import complex.Complex;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import settings.GlobalSettings;

/**
 * Represents a region of the viewport, allowing multithreaded computation
 * of pixel data for each region, which can then be stitched together to form
 * the complete fractal image.
 */
public class Chunk implements Runnable {
  final double log2 = Math.log(2);
  final int size;
  final int[] position;
  GlobalSettings settings;
  final double epsilon = Math.pow(10, -6);
  final double aspectRatio;
  final double scaleConstant;
  Color[][] iterationData;
  boolean skipped = false;
  static int maxThreads = Runtime.getRuntime().availableProcessors();

  /**
   * Create a new Chunk to represent a portion of the viewport.

   * @param size - the size of the chunk in pixels, e.g. 32x32px
   * @param position - the position (x, y) representing the top left corner of the chunk
   */
  public Chunk(int size, int[] position, GlobalSettings settings) {
    this.size = size;
    this.position = position;
    this.settings = settings;
    // compute the aspect ratio and scale constant separately as these are values
    // that are reused millions of times in computing the iteration data
    aspectRatio = settings.width / (double) settings.height;
    scaleConstant = aspectRatio / settings.location.scale;
    iterationData = new Color[size][size];
  }

  /**
   * Create and process the iteration data for all chunks in an image.

   * @param settings - the collection of settings and values global to the whole project
   * @return - a 2D array of chunks that have been processed and are ready to be used
      in the painting pipeline.
   */
  public static Chunk[][] createChunks(int width, int height, GlobalSettings settings) {
    int chunksX = width / 32;
    int chunksY = height / 32;

    Chunk[][] chunks = new Chunk[chunksY][chunksX];
    ExecutorService threadpool = Executors.newFixedThreadPool(Math.max(4, maxThreads - 2));

    for (int y = 0; y < chunksY; y++) {
      for (int x = 0; x < chunksX; x++) {
        chunks[y][x] = new Chunk(32, new int[] {32 * x, 32 * y}, settings);
        threadpool.execute(chunks[y][x]);
      }
    }

    threadpool.shutdown();
    try {
      threadpool.awaitTermination(GlobalSettings.DRAWING_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      System.err.println("Iteration thread interrupted");
    }

    return chunks;
  }

  /**
   * Determine which colouring method to use for each pixel of the fractal image.
   * The user can switch between different modes with their own drawing functions.
   */
  @Override
  public void run() {
    Complex seed = settings.location.seed;
    switch (settings.location.mode) {
      case MANDELBROT:
        skipped = boundaryCheck();
        if (skipped) {
          break;
        }
        for (int y = position[1]; y < position[1] + size; y++) {
          for (int x = position[0]; x < position[0] + size; x++) {
            // if (x % 4 == 0 && y % 4 == 0) {
            //   iterationData[y - position[1]][x - position[0]] = Color.RED;
            //   continue;
            // }
            iterationData[y - position[1]][x - position[0]] = mandelbrotPoint(x, y);
          }
        }
        break;
      case DIVERGENCE_SCHEME:
        for (int y = position[1]; y < position[1] + size; y++) {
          for (int x = position[0]; x < position[0] + size; x++) {
            iterationData[y - position[1]][x - position[0]] = divergenceScheme(x, y);
          }
        }
        break;
      case JULIA:
        for (int y = position[1]; y < position[1] + size; y++) {
          for (int x = position[0]; x < position[0] + size; x++) {
            iterationData[y - position[1]][x - position[0]] = juliaPoint(x, y, seed);
          }
        }
        break;
      default:
        break;
    }
  }

  /**
   * Check the boundary of each chunk, if it is composed only of points contained within the set
   * then we know the interior does not need to be iterated.
   *
   * @return - false if we need to draw the chunk, true if we can skip it.
   */
  private boolean boundaryCheck() {
    // top and bottom boundary
    for (int x = position[0]; x < position[0] + size; x++) {
      int topY = position[1];
      int btmY = position[1] + size - 1;
      if (mandelbrotPoint(x, topY) != Color.BLACK || mandelbrotPoint(x, btmY) != Color.BLACK) {
        return false;
      }
    }
    // left and right boundary (avoid recomputing the corners)
    for (int y = position[1] + 1; y < position[1] + size - 1; y++) {
      int leftX = position[0];
      int rightX = position[0] + size - 1;
      if (mandelbrotPoint(leftX, y) != Color.BLACK || mandelbrotPoint(rightX, y) != Color.BLACK) {
        return false;
      }
    }
    return true;
  }

  private Color estimationRenderer(int x, int y, int radius) {
    return Color.RED;
  }

  private BigDecimal[] arbitraryPrecisionPoint(int x, int y) {
    BigDecimal[] c = new BigDecimal[2];
    BigDecimal[] z = new BigDecimal[2];

    c[0] = new BigDecimal(3 * x).divide(new BigDecimal(settings.width));
    c[1] = new BigDecimal(3 * y).divide(new BigDecimal(settings.height));

    return new BigDecimal[2];
  }

  private Color mandelbrotPoint(int x, int y) {
    Complex c = new Complex(0, 0);
    Complex z = new Complex(0, 0);
    // compute the current location translated into the complex plane
    // and use this as the seed
    double x1 = x / (double) settings.width;
    double y1 = y / (double) settings.height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / settings.location.scale);
    c.subtract(settings.location.center);

    // // if the initial point is within the 'exclusion area' then we know we can skip the iteration
    // if (Math.sqrt(Math.pow((c.re() + 0.25), 2) + Math.pow(c.im(), 2)) <= 0.5) {
    //   return Color.BLACK;
    // }

    // if (Math.sqrt(Math.pow(c.re() + 1, 2) + Math.pow(c.im(), 2)) <= 0.25) {
    //   return Color.BLACK;
    // }

    int iterations = 0;
    while (iterations < settings.location.maxIterations) {
      z.square();
      z.add(c);

      if (z.magnitude() > 4) {
        break;
      }
      iterations++;
    }
    
    // perform the convergence test on each interior point
    if (iterations == settings.location.maxIterations) {
      return Color.BLACK;
    }
    return Colouring.continuousColouring(iterations, z, settings);
  }

  private Color divergenceScheme(int x, int y) {
    Complex c = new Complex(0, 0);
    Complex z = new Complex(0, 0);
    // compute the current location translated into the complex plane
    // and use this as the seed
    double x1 = x / (double) settings.width;
    double y1 = y / (double) settings.height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / settings.location.scale);

    int iterations = 0;
    while (iterations < settings.location.maxIterations) {
      z.square();
      z.add(c);
      z.subtract(settings.location.center);

      if (z.magnitude() > 4) {
        if (iterations % 2 == 0) {
          return Color.BLACK;
        }
        return Color.WHITE;
      }
      iterations++;
    }
    if (iterations == settings.location.maxIterations) {
      return Color.BLACK;
    }
    return new Color(iterations % 255);
  }

  private Color juliaPoint(int x, int y, Complex seed) {
    Complex z = new Complex();
    double x1 = x / (double) settings.width;
    double y1 = x / (double) settings.height;
    z.setRe((3 * x1 - 2) * scaleConstant);
    z.setIm((3 * y1 - 1.25) / settings.location.scale);
    

    return Color.BLUE;
  }
}