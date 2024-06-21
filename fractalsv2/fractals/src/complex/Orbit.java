package complex;

/**
 * Represents an orbit in complex space, under the mandelbrot equation z = z^2 + c.
 * An orbit is the path followed at the infinite limit of this iteration, as we cannot calculuate
 * this infinite limit directly we work up to maxIterations.
 */
public class Orbit {
  @SuppressWarnings("memberName")
  Complex z;
  @SuppressWarnings("memberName")
  Complex c;
  Complex[] previous;
  int maxIterations;
  private final double epsilon = 0.00000001;

  /**
   * Create a new orbit based on the seed point c.

   * @param c - the starting point of the orbit.
   */
  public Orbit(Complex c, int maxIterations) {
    this.c = c;
    this.z = new Complex();
    this.previous = new Complex[maxIterations];
    this.maxIterations = maxIterations;
  }

  public void next() {
    z.square();
    z.add(c);
  }

  /**
   * Finds the first occurrence of c in the array of previous points.

   * @param c - the point to check against the previously visited points
   * @return - the index of the first ocurrence, -1 otherwise
   */
  private int getPreviousValue(Complex c) {
    for (int i = 0; i < maxIterations; i++) {
      // we can guarantee values are inserted in order with no gaps
      // so break on our first null value to save time
      if (previous[i] == null) {
        return -1;
      }
      if (c.equals(previous[i])) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Determine whether the orbit converges to a k-cycle.

   * @return 1 if the orbit converges to a point
      k if the orbit converges to a k-cycle
      maxIterations if the orbit diverges
   */
  public int convergenceTest() {
    Complex prev = new Complex();

    for (int i = 0; i < maxIterations; i++) {
      prev = new Complex(z.re(), z.im());
      next();
      int prevVal = getPreviousValue(z);
      if (prevVal != -1) {
        // our first repeated value is clearly the end of the first cycle
        // so the size of the cycle is the distance between the first occurrence
        // and the first repetition
        return i - prevVal;
      }
      previous[i] = new Complex(z.re(), z.im());
      // it is proven that if |z|^2 exceeds 4, the orbit always diverges 
      if (z.magnitude() > 4) {
        return maxIterations;
      }
      Complex temp = new Complex(z.re(), z.im());
      temp.subtract(prev);
      if (temp.magnitude() < epsilon) {
        return 1;
      }
    }
    // assume any point that hasn't escaped yet must converge to a point
    return 1;
  }


}
