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
  final double[] center;
  final int maxIterations;
  final double scale;
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
      double[] center, int maxIterations, double scale) {
    this.size = size;
    this.type = type;
    this.width = width;
    this.height = height;
    this.position = position;
    this.center = center;
    this.maxIterations = maxIterations;
    this.scale = scale;
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
          case JULIA:
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
    double[] c = new double[] {0, 0};
    double[] z = new double[] {0, 0};
    // compute the current location translated into the complex plane
    // and use this as the seed
    c[0] = (((x / (double) width) * 3) / scale) - (2 / scale);
    c[1] = ((((y / (double) height) * 3) / scale) - (1.25 / scale));

    int iterations = 0;
    while (iterations < maxIterations) {
      double tempRe = z[0];
      z[0] = ((z[0] * z[0] - z[1] * z[1]) + c[0]) - center[0];
      z[1] = (2 * tempRe * z[1] + c[1]) - center[1];

      if (z[0] * z[0] + z[1] * z[1] > 2) {
        break;
      }
      iterations++;
    }
    return iterations;
  }
}
