/**
 * Represents a region of the viewport, allowing multithreaded computation
 * of pixel data for each region, which can then be stitched together to form
 * the complete fractal image.
 */
public class Chunk implements Runnable {
  final int size;
  final Fractals type;
  final int width;
  final int height;
  final int[] position;
  final Complex center;
  final Complex seed;
  final int maxIterations;
  final double scale;
  final int boxSize = 3;
  final double epsilon = Math.pow(10, -8);
  final double aspectRatio;
  final double scaleConstant;
  int[][] iterationData;

  /**
   * Create a new Chunk to represent a portion of the viewport.

   * @param size - the size of the chunk in pixels, e.g. 32x32px
   * @param type - A value in the fractals enumeration representing the fractal to be processed
   * @param position - the position (x, y) representing the top left corner of the chunk
   * @param width - the width of the entire viewport
   * @param height - the height of the entire viewport
   * @param center - the center co-ordinate x + yi in complex space
   * @param maxIterations - the number of iterations before we determine that a point
    never escapes - higher maximum means longer processing times but (much) higher quality
   * @param scale - the scale/zoom factor of the image, larger scale means higher zoom
   */
  public Chunk(int size, Fractals type, int[] position, int width, int height,
      Complex center, Complex seed, int maxIterations, double scale) {
    this.size = size;
    this.type = type;
    this.width = width;
    this.height = height;
    this.position = position;
    this.center = center;
    this.seed = seed;
    this.maxIterations = maxIterations;
    this.scale = scale;
    // compute the aspect ratio and scale constant separately as these are values
    // that are reused millions of times in computing the iteration data
    aspectRatio = width / (double) height;
    scaleConstant = aspectRatio / scale;
    iterationData = new int[size][size];
  }

  @Override
  public void run() {
    for (int y = position[1]; y < position[1] + size; y++) {
      for (int x = position[0]; x < position[0] + size; x++) {
        switch (type) {
          case MANDELBROT:
            iterationData[y - position[1]][x - position[0]] = mandelbrotPoint(x, y);
            break;
          case INTERIOR_POINT:
            iterationData[y - position[1]][x - position[0]] = interiorPoint(x, y);
            break;
          case JULIA:
            iterationData[y - position[1]][x - position[0]] = juliaPoint(x, y, seed);
            break;
          case BURNING_SHIP:
            break;
          default:
            break;
        }
      }
    }
  }

  private int mandelbrotPoint(int x, int y) {
    Complex c = new Complex(0, 0);
    Complex z = new Complex(0, 0);
    // compute the current location translated into the complex plane
    // and use this as the seed
    double x1 = x / (double) width;
    double y1 = y / (double) height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / scale);

    int iterations = 0;
    while (iterations < maxIterations) {
      z.square();
      z.add(c);
      z.subtract(center);

      if (z.magnitude() > 4) {
        break;
      }
      iterations++;
    }
    return iterations;
  }

  private int interiorPoint(int x, int y) {
    Complex c = new Complex(0, 0);
    Complex z = new Complex(0, 0);
    // compute the current location translated into the complex plane
    // and use this as the seed
    double x1 = x / (double) width;
    double y1 = y / (double) height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / scale);

    int iterations = 0;
    while (iterations < maxIterations) {
      z.square();
      z.add(c);
      z.subtract(center);

      if (z.magnitude() > 4) {
        if (iterations % 2 == 0) {
          return 1000;
        }
        return 254;
      }
      iterations++;
    }
    return iterations;
  }

  private int juliaPoint(int x, int y, Complex seed) {
    Complex z = new Complex(0, 0);
    z.setRe((((x / (double) width) * (4 * aspectRatio)) / scale) - ((2 * aspectRatio) / scale));
    z.setIm((((y / (double) height)) * 4 / scale) - (2 / scale));

    int iterations = 0;
    while (iterations < maxIterations) {
      z.square();
      z.add(seed);
      z.subtract(center);

      if (z.magnitude() > 4) {
        break;
      }
      iterations++;
    }
    return iterations;
  }
}
