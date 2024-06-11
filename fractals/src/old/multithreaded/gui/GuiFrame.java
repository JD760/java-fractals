package old.multithreaded.gui;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * Represents the GUI window of the fractals application.
 */
public class GuiFrame extends Frame {
  /**
   * Create a new instance of the gui, with a specified canvas and dimensions.

   * @param width - width of the application window
   * @param height - height of the application window
   * @param canvas - the canvas to display (containing drawn pixel data)
   */
  public GuiFrame(int width, int height, FractalCanvas canvas) {
    setSize(width, height);
    System.out.println(width + "  " + height);
    setResizable(false);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    addWindowStateListener(new WindowStateListener() {
      public void windowStateChanged(WindowEvent e) {
        if ((e.getNewState() & MAXIMIZED_BOTH) == MAXIMIZED_BOTH) {
          System.out.println(getHeight());
        }
      }
    });

    add(canvas);
    setTitle("Java Fractals");
    setVisible(true);
  }
}