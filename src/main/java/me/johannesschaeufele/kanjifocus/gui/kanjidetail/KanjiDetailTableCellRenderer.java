package me.johannesschaeufele.kanjifocus.gui.kanjidetail;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import me.johannesschaeufele.kanjifocus.database.user.Progress;
import me.johannesschaeufele.kanjifocus.gui.ValuePair;

public class KanjiDetailTableCellRenderer extends JLabel implements TableCellRenderer {

    private final Border border = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);

    public KanjiDetailTableCellRenderer() {
        this.setOpaque(true);

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 25);
        this.setFont(font);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof ValuePair) {
            ValuePair valuePair = (ValuePair) value;

            this.setText(valuePair.getValue());

            Progress progress = valuePair.getProgress();

            this.setBackground(progress.getColor());
            this.setBorder(this.border);
        }
        else {
            this.setText(null);

            this.setBackground(Color.GRAY);
            this.setBorder(null);
        }

        if(column <= 1) {
            this.setHorizontalAlignment(CENTER);
        }
        else {
            this.setHorizontalAlignment(LEFT);
        }

        return this;
    }

}
