package gui.menu;

import chunk.ChunkPainter;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import settings.GlobalSettings;

/**
 * Represents the File menu, with the associated commands and functionality
 * such as the image save function.
 */
public class FileMenu extends JMenu {
  private GlobalSettings settings;
  private JMenuItem saveImage = new JMenuItem("Save Image");
  private JTextField widthField = new JTextField("3840");
  private JTextField heightField = new JTextField("2144");
  private JPanel dimensionsPanel = new JPanel();
  private JFileChooser fileChoose = new JFileChooser();

  /**
   * Create a new File menu instance and set up the initial values of the labels.

   * @param settings - global settings and values for the whole project
   */
  public FileMenu(GlobalSettings settings) {
    super("File");
    this.settings = settings;

    dimensionsPanel.setLayout(new GridLayout(2, 2));
    dimensionsPanel.add(new JLabel("x: "));
    dimensionsPanel.add(widthField);
    dimensionsPanel.add(Box.createHorizontalStrut(15));
    dimensionsPanel.add(new JLabel("y: "));
    dimensionsPanel.add(heightField);
    saveImage.addActionListener(new SaveImageAction());
    add(saveImage);
  }
  
  class SaveImageAction extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      int result = JOptionPane.showConfirmDialog(
          settings.panel,
          dimensionsPanel,
          "Enter image dimensions",
          JOptionPane.OK_CANCEL_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null
      );
      if (result == JOptionPane.OK_OPTION) {
        boolean valid = testValidInput(widthField) && testValidInput(heightField);
        if (!valid) {
          JOptionPane.showMessageDialog(
              settings.panel,
              "Invalid dimensions \nx: " + widthField.getText() + "\ny: " + heightField.getText(),
              "Dimension error",
              JOptionPane.ERROR_MESSAGE
          );
          widthField.setText("3840");
          heightField.setText("2144");
          return;
        }
        // we can guarantee now that the width and height may be parsed
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        width = width - (width % 32);
        height = height - (height % 32);
        // if the user presses cancel, or otherwise leaves the dialog, do nothing
        if (fileChoose.showSaveDialog(settings.panel) != JFileChooser.APPROVE_OPTION) {
          return;
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        ChunkPainter.paintChunks(width, height, img.getGraphics(), settings);
        System.out.println("Created image data");
        File selected = fileChoose.getSelectedFile();
        File imageFile = new File(selected.toString() + ".png");
        try {
          ImageIO.write(img, "PNG", imageFile);
        } catch (IOException ioe) {
          System.out.println(ioe);
          return;
        }
      }
    }

    private boolean testValidInput(JTextField field) {
      try {
        Integer.parseInt(field.getText());
      } catch (NumberFormatException nfe) {
        field.setText("Invalid size...");
        return false;
      }
      return true;
    }
  }
}
