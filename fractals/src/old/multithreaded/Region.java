package old.multithreaded;

/**
 * Defines a square region of pixels with size SIZE.
 */
public class Region {
  int size = Util.REGION_SIZE;
  int[][] data;
  private int[] position; // top-left corner of the region

  public Region(int x, int y) {
    this.position = new int[] {x, y};
  }

  public int getX() {
    return position[0];
  }

  public int getY() {
    return position[1];
  }

  /**
   * Determines whether the perimeter of a region is made only of black cells.
   * Due to the connectedness of the mandelbrot set, this means the region is entirely
   * black so we can avoid computing its' inner colour

   * @return - true if the perimeter of the region is entirely black, false otherwise
   */
  public boolean withinSet() {
    boolean isFilled = true;
    for (int i = 0; i < size; i++) {
      // top row
      if (data[0][i] != Util.MAX_ITERATIONS) {
        isFilled = false;
      }
      // bottom row
      if (data[size - 1][i] != Util.MAX_ITERATIONS) {
        isFilled = false;
      }
      // left row
      if (data[i][0] != Util.MAX_ITERATIONS) {
        isFilled = false;
      }
      // right row
      if (data[i][size - 1] != Util.MAX_ITERATIONS) {
        isFilled = false;
      }
    }
    return isFilled;
  }
}
