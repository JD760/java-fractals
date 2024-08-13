package complex;

/**
 * Represents a point (x, y) in 2D space.
 */
public class Point {
  @SuppressWarnings("MemberName")
  public int x;
  @SuppressWarnings("MemberName")
  public int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point() {
    this(0, 0);
  }
}
