package complex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains a collection of tests to ensure the orbit convergence test is accurate
 * for a selection of points in the complex plane.
 */
public class OrbitTests {
  @Test
  public void testNext() {
    Orbit o1 = new Orbit(new Complex(-0.37, 0.39), 1000);
    o1.next();
    o1.next();
    o1.next();
    assertEquals(-0.23, o1.z.re(), 0.01);
    assertEquals(0.31, o1.z.im(), 0.01);
  }

  @Test
  public void testPointConverges() {
    Orbit o1 = new Orbit(new Complex(-0.37, 0.39), 1000);
    assertEquals(1, o1.convergenceTest());
  }

  @Test
  public void test2Cycle() {
    Orbit o1 = new Orbit(new Complex(0.9291666666666667, -0.019936708860759378), 1000);
    assertEquals(2, o1.convergenceTest());
  }

  @Test
  public void test3Cycle() {
    Orbit o1 = new Orbit(new Complex(-0.129, 0.731), 1000);
    assertEquals(3, o1.convergenceTest());
  }

  @Test
  public void testValidPoint() {
    Orbit o1 = new Orbit(new Complex(-0.9833333333333334, 0.17104430379746838), 1000);
    assertEquals(2, o1.convergenceTest());
  }

  @Test
  public void test5Cycle() {
    Orbit o1 = new Orbit(new Complex(-0.513, 0.543), 1000);
    assertEquals(5, o1.convergenceTest());
  }

  @Test
  public void testDivergence() {
    Orbit o1 = new Orbit(new Complex(-0.784, 0.613), 1000);
    assertEquals(1000, o1.convergenceTest());
  }
}
