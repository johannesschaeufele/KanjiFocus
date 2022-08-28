package me.johannesschaeufele.kanjifocus.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import me.johannesschaeufele.kanjifocus.database.user.Progress;

public class VocabularyTableCellRenderer extends JLabel implements TableCellRenderer {

    private final Border border = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK);

    private final Font fontVocabulary = new Font(Font.SANS_SERIF, Font.PLAIN, 25);
    private final Font fontSenses = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

    public VocabularyTableCellRenderer() {
        this.setOpaque(true);

        this.setHorizontalAlignment(LEFT);
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

        if(column == 0) {
            this.setFont(this.fontVocabulary);
        }
        else {
            this.setFont(this.fontSenses);
        }

        return this;
    }

}
