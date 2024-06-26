package chunk;

import complex.Complex;
import complex.Orbit;
import java.awt.Color;
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
    scaleConstant = aspectRatio / settings.scale;
    iterationData = new Color[size][size];
  }

  /**
   * Determine which colouring method to use for each pixel of the fractal image.
   * The user can switch between different modes with their own drawing functions.
   */
  @Override
  public void run() {
    Complex seed = settings.seed;
    for (int y = position[1]; y < position[1] + size; y++) {
      for (int x = position[0]; x < position[0] + size; x++) {
        switch (settings.mode) {
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
    double x1 = x / (double) settings.width;
    double y1 = y / (double) settings.height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / settings.scale);

    int iterations = 0;
    while (iterations < settings.maxIterations) {
      z.square();
      z.add(c);
      z.subtract(settings.center);

      if (z.magnitude() > 4) {
        break;
      }
      iterations++;
    }
    
    // perform the convergence test on each interior point
    if (iterations == settings.maxIterations) {
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
    double x1 = x / (double) settings.width;
    double y1 = y / (double) settings.height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / settings.scale);

    int iterations = 0;
    while (iterations < settings.maxIterations) {
      z.square();
      z.add(c);
      z.subtract(settings.center);

      if (z.magnitude() > 4) {
        if (iterations % 2 == 0) {
          return Color.BLACK;
        }
        return Color.WHITE;
      }
      iterations++;
    }
    if (iterations == settings.maxIterations) {
      return Color.BLACK;
    }
    return new Color(iterations % 255);
  }

  private Color juliaPoint(int x, int y, Complex seed) {
    Complex z = new Complex(0, 0);
    double x1 = x / (double) settings.width;
    double y1 = y / (double) settings.height;
    z.setRe((4 * x1 - 2) * scaleConstant);
    //z.setRe(((x1 * (4 * aspectRatio)) / scale) - ((2 * aspectRatio) / scale));
    z.setIm((4 * y1 - 2) / settings.scale);
    z.subtract(settings.center);

    int iterations = 0;
    while (iterations < settings.maxIterations) {
      z.square();
      z.add(seed);

      if (z.magnitude() > 4) {
        break;
      }
      iterations++;
    }
    if (iterations == settings.maxIterations) {
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
    double x1 = x / (double) settings.width;
    double y1 = y / (double) settings.height;
    z.setRe((4 * x1 - 2) * scaleConstant);
    z.setIm((4 * y1 - 2) / settings.scale);
    z.subtract(settings.center);
  
    int iterations = 0;
    while (iterations < settings.maxIterations) {
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
    double x1 = x / (double) settings.width;
    double y1 = y / (double) settings.height;
    c.setRe((3 * x1 - 2) * scaleConstant);
    c.setIm((3 * y1 - 1.25) / settings.scale);


    //c.subtract(center);
    Orbit orbit = new Orbit(c, settings.maxIterations);
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
