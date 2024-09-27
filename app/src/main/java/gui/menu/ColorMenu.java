package gui.menu;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import settings.GlobalSettings;

/**
 * Allows colour settings to be changed on the fly, redrawing the canvas so the user can see the
 * effect of their changes immediately. Extends the JMenu class to provide a custom menu design.
 */
public class ColorMenu extends JMenu {
  private GlobalSettings settings;

  /**
   * Create a new colour menu instance.

   * @param settings - a collection of settings and values global to the whole project.
   */
  public ColorMenu(GlobalSettings settings) {
    super("Coloring Options");
    this.settings = settings;

    add(new FrequencyControl("Red"));
    add(new FrequencyControl("Green"));
    add(new FrequencyControl("Blue"));
  }

  /**
   * Creates a frequency slider constrained between 0 and 0.1
   * Each slider covers a specific colour channel and is linked to the colorSettings entry.
   */
  class FrequencyControl extends JPanel {
    private JLabel freqLabel;
    private JSlider freqSlider;
    private String channel;
    private int sliderValue = 13;

    public FrequencyControl(String channel) {
      this.channel = channel;
      setLayout(new GridLayout(2, 1));
      freqLabel = new JLabel(channel + " Frequency - " + Double.toString(sliderValue / 1000.0));
      freqSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 13);

      freqSlider.addChangeListener(new FreqChangeListener());

      add(freqLabel);
      add(freqSlider);
    }

    /**
     * Listens for changes in the frequency slider and updates the global settings
     * to reflect the changes.
     */
    class FreqChangeListener implements ChangeListener {
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        sliderValue = source.getValue();
        //System.out.println(sliderValue);
        freqLabel.setText(channel + " Frequency - " + Double.toString(sliderValue / 1000.0));
        switch (channel) {
          case "Red":
            settings.colorSettings.redFreq = sliderValue / 1000.0;
            break;
          case "Blue":
            settings.colorSettings.blueFreq = sliderValue / 1000.0;
            break;
          case "Green":
            settings.colorSettings.greenFreq = sliderValue / 1000.0;
            break;
          default:
            break;
        }
        settings.panel.repaint();
      }
    }
  }
}
