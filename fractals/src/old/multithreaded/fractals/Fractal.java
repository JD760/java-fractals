package old.multithreaded.fractals;

import java.util.concurrent.ArrayBlockingQueue;

import old.multithreaded.Region;
import old.multithreaded.Util;

public class Fractal {
  public Fractal() {

  }

  /**
   * Creates the regions of the image, and initialises a queue of Region objects to be processed.

   * @return - A Queue of Region objects of size (HEIGHT / REGION_SIZE) * (WIDTH / REGION_SIZE)
   */
  public ArrayBlockingQueue<Region> createRegions() {
    int regionsWidth = Util.WIDTH / Util.REGION_SIZE;
    int regionsHeight = Util.HEIGHT / Util.REGION_SIZE;
    System.out.println(regionsHeight);
    ArrayBlockingQueue<Region> regionQueue = new ArrayBlockingQueue<>(regionsHeight * regionsWidth);

    // we can guarantee these are integers as we check against this in the main function
    for (int y = 0; y < regionsHeight; y++) {
      for (int x = 0; x < regionsWidth; x++) {
        regionQueue.add(new Region(Util.REGION_SIZE * x, Util.REGION_SIZE * y));
      }
    }
    return regionQueue;
  }
}
