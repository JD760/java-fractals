package gui.interestingpoints;

import chunk.ChunkPainter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import complex.Complex;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import settings.Fractals;
import settings.GlobalSettings;
import settings.Location;
import utils.Utils;

/**
 * Represents the Interesting Points feature, which allows the user to log
 * and sort positions in the fractal that they may return to later.
 */
public class InterestingPoints extends JPanel {
  private JsonNode[] data;
  private JTable table = new JTable(new TableModel());
  private Location selected;
  private JLabel nameLabel = new JLabel("Name: ");
  private JLabel tagsLabel = new JLabel("Tags: ");
  private JLabel centerLabel = new JLabel("Center: ");
  private JLabel seedLabel = new JLabel("Seed: ");
  private JLabel scaleLabel = new JLabel("Scale: ");
  private JLabel maxIterationsLabel = new JLabel("Max Iterations: ");
  private JLabel modeLabel = new JLabel("Mode: ");
  private BufferedImage im = new BufferedImage(128, 128, BufferedImage.TYPE_3BYTE_BGR);
  private JLabel imageLabel = new JLabel(new ImageIcon(im));

  /**
   * Create a new UI element to display the interesting points GUI, allowing viewing
   * and preview of saved points.
   */
  public InterestingPoints(Location selected) {
    this.selected = selected;
    ObjectNode json = Utils.fileToJson(new File(GlobalSettings.pointLogPath));
    Iterator<JsonNode> elements = json.elements();
    data = new JsonNode[json.size()];

    int i = 0;
    while (elements.hasNext()) {
      data[i] = elements.next();
      i++;
    }

    add(new JScrollPane(table));
    add(new LabelContainer());
    add(imageLabel);

    table.addMouseListener(new TableClickListener());
    table.setFillsViewportHeight(true);

  }

  class LabelContainer extends JPanel {
    public LabelContainer() {
      setLayout(new GridLayout(0, 1));
      JLabel spacingLabel = new JLabel("Unfortunately needed as a hacky fix");
      add(spacingLabel);
      spacingLabel.setVisible(false);
      add(nameLabel);
      add(tagsLabel);
      add(centerLabel);
      add(seedLabel);
      add(scaleLabel);
      add(maxIterationsLabel);
      add(modeLabel);
    }
  }

  class TableClickListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      int row = table.getSelectedRow();
      JsonNode rowData = data[row];
      selected.update(
          new Complex(
            rowData.get("Re(center)").asDouble(),
            rowData.get("Im(center)").asDouble()
          ),
          new Complex(),
          Double.parseDouble(rowData.get("scale").asText()),
          rowData.get("maxIterations").asInt(),
          Fractals.getElement(rowData.get("mode").asText())
      );
      
      nameLabel.setText("Name: " + (String) table.getValueAt(row, 0));
      tagsLabel.setText("Tags: " + (String) table.getValueAt(row, 1));
      centerLabel.setText("Center: " + selected.center.toRoundedString(3));
      seedLabel.setText("Seed: " + selected.seed.toRoundedString(3));
      scaleLabel.setText("Scale: " + selected.scale);
      maxIterationsLabel.setText("Max Iterations: " + selected.maxIterations);
      modeLabel.setText("Mode: " + selected.mode);

      //BufferedImage im = new BufferedImage(128, 128, BufferedImage.TYPE_3BYTE_BGR);
      GlobalSettings iconSettings = new GlobalSettings(128, 128, selected);
      ChunkPainter.paintChunks(128, 128, im.createGraphics(), iconSettings);
      imageLabel.setIcon(new ImageIcon(im));
    }
  }

  class TableModel extends AbstractTableModel {
    private String[] columnNames = {"Name", "Tags", "Fractal Type"};
    

    public int getColumnCount() {
      return columnNames.length;
    }

    public int getRowCount() {
      return data.length;
    }

    public String getColumnName(int col) {
      return columnNames[col];
    }

    public JsonNode getRow(int row) {
      return data[row];
    }

    public Object getValueAt(int row, int col) {
      JsonNode rowData = data[row];
      switch (col) {
        case 0:
          return rowData.get("Name").asText();
        case 1:
          return rowData.get("Tags").asText();
        case 2:
          // Find the appropriate localised name for the mode by searching the Fractals enum
          // for example MANDELBROT becomes "Mandelbrot Set"
          return Fractals.getElement(rowData.get("mode").asText());
        default:
          return "";
      }
    }

    public Class<?> getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }
  }
}
