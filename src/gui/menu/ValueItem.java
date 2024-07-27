package gui.menu;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import settings.GlobalSettings;

/**
 * Represents a menu item with extra funtionality such as click to modify.
 */
public class ValueItem extends JMenuItem {
  private GlobalSettings settings;

  public ValueItem(GlobalSettings settings) {
    this.settings = settings;
    addActionListener(new ClickAction());
  }

  /**
   * When the user clicks a ValueItem, a menu should be created
   * allowing the value to be modified or reset.
   */
  public class ClickAction extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      JOptionPane.showConfirmDialog(settings.panel, "Test", "Test", JOptionPane.OK_CANCEL_OPTION);
    }
  }


}
