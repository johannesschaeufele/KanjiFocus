package me.johannesschaeufele.kanjifocus.gui.kanjioverview;

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

public class KanjiOverviewTableCellRenderer extends JLabel implements TableCellRenderer {

    private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 25);
    private final Font fontSmall = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    private final Font fontLarge = new Font(Font.SANS_SERIF, Font.PLAIN, 40);

    private final Border border = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);

    public KanjiOverviewTableCellRenderer() {
        this.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Progress progress = null;
        if(value instanceof ValuePair) {
            ValuePair valuePair = (ValuePair) value;

            this.setText(valuePair.getValue());

            progress = valuePair.getProgress();

            this.setBorder(this.border);
        }
        else {
            this.setText(null);
            this.setBorder(null);
        }

        if(progress == null) {
            this.setBackground(Color.GRAY);
        }
        else {
            this.setBackground(progress.getColor());
        }

        if(column == 0) {
            this.setFont(fontLarge);
            this.setHorizontalAlignment(CENTER);
        }
        else if(column == 1) {
            this.setFont(fontSmall);
            this.setHorizontalAlignment(CENTER);
        }
        else {
            this.setFont(font);
            this.setHorizontalAlignment(LEFT);
        }

        return this;
    }

}
