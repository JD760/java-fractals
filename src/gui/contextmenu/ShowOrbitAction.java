package gui.contextmenu;

import complex.Complex;
import complex.Orbit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import settings.GlobalSettings;
import settings.Location;
import utils.Utils;

/**
 * Invoked when the user selects the 'Show Orbit' context item.
 */
public class ShowOrbitAction extends AbstractAction {
  private GlobalSettings settings;
  private Location location;
  private int x;
  private int y;

  public ShowOrbitAction(GlobalSettings settings, int x, int y) {
    this.settings = settings;
    this.location = settings.location;
    this.x = x;
    this.y = y;
  }

  public void actionPerformed(ActionEvent e) {
    Orbit orbit = new Orbit(Utils.getComplexPoint(
          settings.mouseX,
          settings.mouseY,
          settings.width,
          settings.height,
          location.scale),
        location.maxIterations);
    Graphics g = settings.panel.getGraphics();
    g.setColor(Color.RED);
    for (int i = 0; i < 10; i++) {
      stepOrbit(g, orbit);
    }
  }

  private void stepOrbit(Graphics g, Orbit orbit) {
    int[] origin = Utils.getCanvasPoint(
        orbit.getPoint(), settings.width, settings.height, location.scale, location.center);
    orbit.next();
    int[] dest = Utils.getCanvasPoint(
        orbit.getPoint(), settings.width, settings.height, location.scale, location.center);
    g.drawLine(origin[0], origin[1], dest[0], dest[1]);
  }
}
