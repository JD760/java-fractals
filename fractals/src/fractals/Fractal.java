package fractals;

/**
 * Creates a new fractal, which encodes image data into an array which may then be 
 * passed to a colouring function.
 */
public class Fractal {
  private int width;
  private int height;
  private double scale;
  private int[][] iterationData;
  private int maxIterations = 1000;

  /**
   * Creates a new instance of the fractal.

   * @param width - width of the image to be created
   * @param height - height of the image to be created
   * @param scale - zoom level of the image on the fractal
   */
  public Fractal(int width, int height, double scale) {
    this.width = width;
    this.height = height;
    this.scale = scale;
    this.iterationData = new int[height][width];
  }

  public Fractal(int width, int height) {
    this(width, height, 1);
  }

  public int[][] getIterationData() {
    return iterationData;
  }


  /**
   * Draws a new mandelbrot and fills the iterationData array with values.
   */
  public void mandelbrot() {
    // complex numbers are defined as double[2] with the real and imaginary components
    // being the 0th and 1st index respectively
    double[] c = {0, 0};
    double[] z = {0, 0};
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        // get the positions of the seed point in the complex plane
        c[0] = (x / (double) width) * (4 * scale) - (2.5 * scale);
        c[1] = (y / (double) width) * (4 * scale) - (1.0 * scale);
        int iterations = 0;

        z[0] = 0;
        z[1] = 0;
        while (iterations < maxIterations) {
          double tempRe = z[0];
          //System.out.println(z[0] + " + " + z[1] + "i");
          // apply the formula z = z^2 + c to get the behaviour of the points
          z[0] = (z[0] * z[0]) - (z[1] * z[1]) + c[0];
          z[1] = 2 * tempRe * z[1] + c[1];
          // it can be proven that if |z| > 2 then the point is not in the set
          if (z[0] * z[0] + z[1] * z[1] >= 4) {
            break;
          }
          iterations++;
        }
        iterationData[y][x] = iterations;
      }
      if (y % 10 == 0) {
        System.out.println(y);
      }
    }
  }
}
