import complex.Complex;
import complex.Orbit;
import java.awt.Color;

/**
 * Represents a region of the viewport, allowing multithreaded computation
 * of pixel data for each region, which can then be stitched together to form
 * the complete fractal image.
 */
public class Chunk implements Runnable {
  final double log2 = Math.log(2);
  final int size;
  final Fractals type;
  final int width;
  final int height;
  final int[] position;
  final Complex center;
  final Complex seed;
  final int maxIterations;
  final double scale;
  final double epsilon = Math.pow(10, -6);
  final double aspectRatio;
  final double scaleConstant;
  Color[][] iterationData;

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
    iterationData = new Color[size][size];
  }

  /**
   * Determine which colouring method to use for each pixel of the fractal image.
   * The user can switch between different modes with their own drawing functions.
   */
  @Override
  public void run() {
    for (int y = position[1]; y < position[1] + size; y++) {
      for (int x = position[0]; x < position[0] + size; x++) {
        switch (type) {
          case MANDELBROT:
            iterationData[y - position[1]][x - position[0]] = mandelbrotPoint(x, y);
            break;
          case DIVERGENCE_SCHEME:
            iterationData[y - position[1]][x - position[0]] = divergenceScheme(x, y);
            break;
          case JULIA:
            iterationData[y - position[1]][x - position[0]] = juliaPoint(x, y, seed);
            break;
          case JULIA_DIVERGENCE:
            iterationData[y - position[1]][x - position[0]] = juliaDivergence(x, y, seed);
            break;
          default:
            break;
        }
      }
    }
  }

  private Color mandelbrotPoint(int x, int y) {
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
    
    // perform the convergence test on each interior point
    if (iterations == maxIterations) {
      return Color.BLACK;
    }
    double continuousIndex = iterations + 1 - ((log2 / z.magnitude()) / log2);
    int red = (int) Math.abs((Math.sin(0.01 * continuousIndex + 4) * 230) + 25);
    int blue = (int) Math.abs((Math.sin(0.016 * continuousIndex + 2) * 230) + 25);
    int green = (int) Math.abs((Math.sin(0.01 * continuousIndex + 1) * 230) + 25);
    //return new Color(iterations % 255, iterations % 255, iterations % 255);
    return new Color(red, blue, green);
  }

  private Color divergenceScheme(int x, int y) {
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
          return Color.BLACK;
        }
        return Color.WHITE;
      }
      iterations++;
    }
    if (iterations == maxIterations) {
      return Color.BLACK;
    }
    return new Color(iterations % 255);
  }

  private Color juliaPoint(int x, int y, Complex seed) {
    Complex z = new Complex(0, 0);
    z.setRe((((x / (double) width) * (4 * aspectRatio)) / scale) - ((2 * aspectRatio) / scale));
    z.setIm((((y / (double) height)) * 4 / scale) - (2 / scale));
    z.subtract(center);

    int iterations = 0;
    while (iterations < maxIterations) {
      z.square();
      z.add(seed);

      if (z.magnitude() > 4) {
        break;
      }
      iterations++;
    }
    if (iterations == maxIterations) {
      return Color.BLACK;
    }

    double continuousIndex = iterations + 1 - ((log2 / z.magnitude()) / log2);
    int red = (int) Math.abs((Math.sin(0.01 * continuousIndex + 4) * 230) + 25);
    int blue = (int) Math.abs((Math.sin(0.016 * continuousIndex + 2) * 230) + 25);
    int green = (int) Math.abs((Math.sin(0.01 * continuousIndex + 1) * 230) + 25);
    //return new Color(iterations % 255, iterations % 255, iterations % 255);
    return new Color(red, blue, green);
  } 

  private Color juliaDivergence(int x, int y, Complex seed) {
    Complex z = new Complex(0, 0);
    z.setRe((((x / (double) width) * (4 * aspectRatio)) / scale) - ((2 * aspectRatio) / scale));
    z.setIm((((y / (double) height)) * 4 / scale) - (2 / scale));

    z.subtract(center);
    int iterations = 0;
    while (iterations < maxIterations) {
      z.square();
      z.add(seed);

      if (z.magnitude() > 4) {
        if (iterations % 2 == 0) {
          return Color.BLACK;
        }
        return Color.WHITE;
      }
      iterations++;
    }
    return new Color(iterations % 255);
  }

  @SuppressWarnings("unused")
  private Color convergenceScheme(int x, int y) {
    Complex c = new Complex(0, 0);
    // compute the current location translated into the complex plane
    // and use this as the seed
    double x1 = x / (double) width;
    double y1 = y / (double) height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / scale);


    //c.subtract(center);
    Orbit orbit = new Orbit(c, maxIterations);
    int result = orbit.convergenceTest();

    switch (result) {
      case 1:
        return Color.CYAN;
      case 2:
        return Color.BLUE;
      case 3:
        return Color.GREEN;
      case 4:
        return Color.PINK;
      default:
        return Color.BLACK;
    }
  }
}
