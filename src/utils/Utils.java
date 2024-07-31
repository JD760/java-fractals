package utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
}
