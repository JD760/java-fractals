import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Create a custom JFrame allowing the behaviour of the main window object to be customised.
 */
public class Frame extends JFrame implements ComponentListener {
  private int prevWidth;
  private int prevHeight;
  private FractalPanel panel;

  /**
   * Create a new custom frame, and link the panel corresponding to the fractal viewport.

   * @param panel - A JPanel object on which the fractal will be drawn
   */
  public Frame(FractalPanel panel) {
    super("Java Fractals");
    getContentPane().addComponentListener(this);
    prevWidth = this.getWidth();
    prevHeight = this.getHeight();
    this.panel = panel;
    add(panel);
    JLabel label1 = new JLabel("Test");
    getContentPane().add(label1, BorderLayout.EAST);
    setVisible(true);
  }

  public void componentShown(ComponentEvent e) {
    return;
  }

  public void componentHidden(ComponentEvent e) {
    return;
  }

  public void componentMoved(ComponentEvent e) {
    return;
  }

  @Override
  public void componentResized(ComponentEvent e) {
    // prevent duplicate events from causing unnecessary redrawing
    if (this.getWidth() != prevWidth || this.getHeight() != prevHeight) {
      this.prevHeight = this.getHeight();
      this.prevWidth = this.getWidth();
      panel.width = getWidth() - 200;
      panel.height = getHeight();
      panel.repaint();
    }
  }
}
