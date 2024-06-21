package complex;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Contains a selection of tests for the Complex type.
 */
public class ComplexTests {
  @Test
  public void testComplexEquality() {
    Complex c1 = new Complex(0.1, 0.2);
    Complex c2 = new Complex(0.1, 0.2);

    assertTrue(c1.equals(c1));
    assertTrue(c1.equals(c2));
  }

  @Test
  public void testComplexSets() {
    Set<Complex> testSet = new HashSet<>();
    Complex c = new Complex(0.1, 0.2);
    testSet.add(c);
    boolean contains = false;
    for (Complex d : testSet) {
      if (c.equals(d)) {
        contains = true;
      }
    }
    assertTrue(contains);
  }
}
