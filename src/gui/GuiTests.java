package gui;

import static org.junit.Assert.assertEquals;

import complex.Complex;
import org.junit.Test;
import settings.Fractals;
import settings.Location;

/**
 * Contains a collection of basic tests for the Location class.
 */
public class GuiTests {
  @Test
  public void testParseLocation() {
    String locationStr = "1.0625,0.37278481012658227,0.21,0.32,2.0,1000,MANDELBROT";
    Location loc = new Location();
    loc.parseString(locationStr);

    assertEquals("MANDELBROT", loc.mode.toString());
    assertEquals(1000, loc.maxIterations);
  }

  @Test
  public void testToString() {
    Location loc = new Location(
        new Complex(0.1, 0.2), new Complex(-0.23, 0.21), 4, 2000, Fractals.DIVERGENCE_SCHEME);
    assertEquals("0.1,0.2,-0.23,0.21,4.0,2000,DIVERGENCE_SCHEME", loc.toString());

  }
}
