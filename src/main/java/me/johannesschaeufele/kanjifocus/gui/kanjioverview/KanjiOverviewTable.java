package me.johannesschaeufele.kanjifocus.gui.kanjioverview;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class KanjiOverviewTable extends JTable {

    public KanjiOverviewTable() {
        super();
        setUI(new KanjiOverviewTableUI());

        //setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowSelectionAllowed(false);
        setCellSelectionEnabled(false);
        setColumnSelectionAllowed(false);
        //setEnabled(false);
        setTableHeader(null);
    }

    @Override
    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
        if(column != 0) {
            return super.getCellRect(row, column, includeSpacing);
        }

        int rc = row & (0xFFFFFFFF ^ 0x01);

        Rectangle rect = super.getCellRect(rc, column, includeSpacing);
        rect.height += getRowHeight(rc + 1);

        return rect;
    }

    @Override
    public int rowAtPoint(Point point) {
        int row = super.rowAtPoint(point);
        if(row < 0) {
            return row;
        }

        int column = super.columnAtPoint(point);

        if(column == 0) {
            row &= (0xFFFFFFFF ^ 0x01);
        }

        return row;
    }

}
