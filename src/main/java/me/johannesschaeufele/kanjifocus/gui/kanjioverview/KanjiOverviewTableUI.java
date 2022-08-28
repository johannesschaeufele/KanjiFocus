package me.johannesschaeufele.kanjifocus.gui.kanjioverview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;

public class KanjiOverviewTableUI extends BasicTableUI {

    @Override
    public void paint(Graphics g, JComponent c) {
        Rectangle r = g.getClipBounds();
        int firstColumn = table.columnAtPoint(new Point(r.x, 0));
        int lastColumn = table.columnAtPoint(new Point(r.x + r.width, 0));

        int firstRow = table.rowAtPoint(new Point(0, r.y));
        int lastRow = table.rowAtPoint(new Point(0, r.y + r.height));
        // -1 is a flag that the ending point is outside the table
        if(lastColumn < 0) {
            lastColumn = table.getColumnCount() - 1;
        }
        if(lastRow < 0) {
            lastRow = table.getRowCount() - 1;
        }

        if((lastRow & 1) == 0) {
            lastRow++;
        }

        for(int column = firstColumn; column <= lastColumn; column++) {
            if(column == 0) {
                for(int row = firstRow; row <= lastRow; row += 2) {
                    Rectangle r1 = table.getCellRect(row, column, true);
                    if(r1.intersects(r)) {
                        paintCell(g, r1, row, column);
                    }
                }
            }
            else {
                for(int row = firstRow; row <= lastRow; row++) {
                    Rectangle r1 = table.getCellRect(row, column, true);
                    if(r1.intersects(r)) {
                        paintCell(g, r1, row, column);
                    }
                }
            }
        }
    }

    private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
        if((row & 1) == 0) {
            Color c = g.getColor();
            g.setColor(table.getGridColor());
            g.drawLine(cellRect.x, cellRect.y, cellRect.x + cellRect.width, cellRect.y);
            g.setColor(c);
        }

        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component component = table.prepareRenderer(renderer, row, column);

        rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
    }

}
