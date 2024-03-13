package colouring;

/**
 * Contains a selection of colouring functions used to change the representation
 * of the input fractal.
 */
public class Colouring {
  int width;
  int height;
  int maxIterations;
  int[][] iterationData;

  /**
   * Create a new instance of the colouring object, containing references to all the necessary
   * data for each colouring function.

   * @param width - image width
   * @param height - image height
   * @param maxIterations - maximum iterations before we conclude a point does not diverge
   * @param iterationData - width*height array of iteration data for each pixel
   */
  public Colouring(int width, int height, int maxIterations, int[][] iterationData) {
    this.width = width;
    this.height = height;
    this.maxIterations = maxIterations;
    this.iterationData = iterationData;
  }

  
  /**
   * Colour the fractal according to the modulus of the iteration count
   * against the maximum pixel value of 255.

   * @return - A length 3 array for the RGB values of each pixel
   */
  public int[][][] modulusColouring() {
    int[][][] pixelData = new int[height][width][3];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        // represents the RGB values of the pixel
        int iterations = iterationData[y][x];
        if (iterations == 0) {
          pixelData[y][x] = new int[] {0, 0, 0};
          continue;
        }
        pixelData[y][x] = new int[] {iterations % 255, iterations % 255, iterations % 255};
      }
    }
    return pixelData;
  }
}
