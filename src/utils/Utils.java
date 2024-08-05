package utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import complex.Complex;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Represents a collection of utility functions related mainly to file handling.
 */
public class Utils {
  /**
   * Read the entire contents of a file into a string.

   * @param file - the file to be read
   * @return - the output string containing the file contents
   */
  public static String readFile(File file) {
    StringBuilder str = new StringBuilder();

    try {
      FileReader reader = new FileReader(file);
      int control = 0;
      while (control != -1) {
        control = reader.read();
        if (control != -1) {
          str.append(Character.toString(control));
        }
      }
      reader.close();
    } catch (FileNotFoundException e) {
      System.err.println("Could not find file: " + file.toString());
    } catch (IOException e) {
      System.out.println("Error while reading file: " + file.toString());
    }

    return str.toString();
  }
  /**
   * Read a text file and attempt to convert the contents to JSON.

   * @param file - the path to the file to be read
   * @return - a Java Object representation of the JSON structure
   */
  public static ObjectNode fileToJson(File file) {
    ObjectNode node = new ObjectNode(null);
    ObjectMapper mapper = new ObjectMapper();
    String jsonStr = readFile(file);

    try {
      node = (ObjectNode) mapper.readTree(jsonStr);
    } catch (JacksonException e) {
      System.err.println("Error while parsing JSON file: " + file.toString());
      return null;
    }

    return node;
  }

  /**
   * Convert a complex point to (x, y) co-ordinates in the canvas.

   * @param p - the complex point to convert
   * @param width - the width of the canvas
   * @param height - the height of the canvas
   * @param scale - the scale factor
   * @param center - the center point of the canvas
   * @return - the (x, y) co-ordinate that represents the point c
   */
  public static int[] getCanvasPoint(
      Complex p, int width, int height, double scale, Complex center) {
    int[] point = new int[2];

    // by subtracting the center point we can treat the center as the origin for further calculation
    p.subtract(center);



    return point;
  }

  /**
   * Get the point in the complex plane associated with a given location on the panel.

   * @param x - The x co-ordinate in the canvas
   * @param y - the y co-ordinate in the canvas
   * @param width - the width of the canvas
   * @param height - the height of the canvas
   * @param scale - the zoom factor, a scale of n means a unit square has a side length n
   * @return - the complex value associated with the point (x, y)
   */
  public static Complex getComplexPoint(int x, int y, int width, int height, double scale) {
    double fractalWidth = 3 / scale;
    Complex c = new Complex();
    double aspectRatio = width / (double) height;
    double x1 = x / (double) width;
    double y1 = y / (double) height;
    c.setRe((3 * x1 - 2) * (aspectRatio / scale));
    c.setIm((3 * y1 - 1.25) / scale);

    return c;
  }
}
