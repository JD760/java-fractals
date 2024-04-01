import java.awt.Point;

public class Chunk implements Runnable {
  int size;
  int width;
  int height;
  int[] position;
  double[] center;
  double scale;
  int[][] iterationData;
  boolean optimised;

  public Chunk(int size, int[] position, int width, int height, double[] center, double scale) {
    this.size = size;
    this.width = width;
    this.height = height;
    this.position = position;
    this.center = center;
    this.scale = scale;
    iterationData = new int[size][size];
    //optimiseChunk();
  }

  public void run() {
    if (optimised) {
      return;
    }
    for (int y = position[1]; y < position[1] + size; y++) {
      for (int x = position[0]; x < position[0] + size; x++) {
        iterationData[y - position[1]][x - position[0]] = iteratePoint(x, y);
      }
    }
  }

  private int iteratePoint(int x, int y) {
    double[] c = new double[] {0, 0};
    double[] z = new double[] {0, 0};
    // compute the current location translated into the complex plane
    // and use this as the seed
    c[0] = (((x / (double) width) * 3) / scale) - (2 / scale);
    c[1] = -((((y / (double) height) * 3) / scale) - (1.25 / scale));

    int iterations = 0;
    while (iterations < 1000) {
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

  /**
   * If the chunk perimeter is entirely made of points that are bounded within the mandelbrot set
   * then we can avoid calculating the interior as we know it must also be fully within the set.
   */
  public void optimiseChunk() {
    boolean sidesContained = true;
    for (int y = position[1]; y < position[1] + size; y++) {
      // check both the left and right sides at once
      int x1 = position[0];
      int x2 = position[0] + size - 1;
      if (iteratePoint(x1, y) != 1000 || iteratePoint(x2, y) != 1000) {
        sidesContained = false;
      }
    }

    for (int x = position[0]; x < position[0] + size; x++) {
      int y1 = position[1];
      int y2 = position[1] + size - 1;
      if (iteratePoint(x, y1) != 1000 || iteratePoint(x, y2) != 1000) {
        sidesContained = false;
      }
    }

    // if no optimisation can be done we need to compute the pixels normally
    if (!sidesContained) {
      optimised = false;
      return;
    }

    System.out.println("Optimised Chunk found");
    // fill the entire chunk and mark as optimised so it is not recomputed later
    for (int y = position[1]; y < position[1] + size; y++) {
      for (int x = position[0]; x < position[0] + size; x++) {
        iterationData[y - position[1]][x - position[0]] = 1000;
      }
    }
    optimised = true;
  }
}
