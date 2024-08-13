package complex;

/**
 * Represents a complex number, with support for complex arithmetic and comparison.
 */
public class Complex {
  private double re;
  private double im;
  private double re2;
  private double im2;
  // due to floating point issues, direct equality testing may cause inaccuracy
  // so we say two numbers are equal when their real and imaginary components
  // are different by less than epsilon
  private final double epsilon = 0.00000001;

  /**
   * Create a new complex number with initial real and complex values.

   * @param re - the real part of the complex number Re(z)
   * @param im - the imaginary/complex part Im(z)
   */
  public Complex(double re, double im) {
    this.re = re;
    this.im = im;
    this.re2 = re * re;
    this.im2 = im * im;
  }

  public Complex() {
    this(0, 0);
  }

  public void setRe(double re) {
    this.re = re;
    re2 = re * re;
  }

  public void setIm(double im) {
    this.im = im;
    im2 = im * im;
  }

  public double re() {
    return re;
  }

  public double im() {
    return im;
  }

  @Override
  public String toString() {
    if (im >= 0) {
      return re + " + " + im + "i";
    }
    return re + " - " + (-1 * im) + "i";
  }

  /**
   * Provides a string representation of the complex number with both the real and complex
   * components rounded to within a certain number of decimal places.

   * @param places - the number of places to truncate to.
   * @return - A String representation of the complex number
   */
  public String toRoundedString(int places) {
    double val = 10 * places;
    if (im >= 0) {
      return (Math.round(re * val) / val) + " + " + (Math.round(im * val) / val) + "i";
    }
    return (Math.round(re * val) / val) + " - " + (Math.round(-1 * im * val) / val) + "i";
  }

  /**
   * Performs addition between two complex numbers x + yi and u + vi
   * The resulting number is z = x + u + (y + v)i.

   * @param num - the complex number to add
   */
  public void add(Complex num) {
    re += num.re;
    re2 = re * re;
    im += num.im;
    im2 = im * im;
  }

  /**
   * Perform complex subraction, treating the real and imaginary components separately.

   * @param num - the number to subtract from this complex number such that z.subtract(w)
    means z - w
   */
  public void subtract(Complex num) {
    re -= num.re;
    re2 = re * re;
    im -= num.im;
    im2 = im * im;
  }

  /**
   * Calculate the square z^2 of a given complex number z.
   */
  public void square() {
    double temp = re;
    re = re2 - im2;
    im = 2 * im * temp;
    re2 = re * re;
    im2 = im * im;
  }

  /**
   * Calculate the square of the magnitude of the complex number
   * without using the square root as this wastes computation time in our application.

   * @return - the value of Re(z)^2 + Im(z)^2
   */
  public double magnitude() {
    return re2 + im2;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Complex)) {
      return false;
    }
    Complex num = (Complex) other;

    double diffRe = re - num.re();
    double diffIm = im - num.im();

    if (diffRe < -1 * epsilon || diffRe > epsilon) {
      return false;
    }
    if (diffIm < -1 * epsilon || diffIm > epsilon) {
      return false;
    }
    return true;
  }
}
