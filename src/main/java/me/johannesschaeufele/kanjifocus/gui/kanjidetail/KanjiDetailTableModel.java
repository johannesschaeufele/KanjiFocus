package me.johannesschaeufele.kanjifocus.gui.kanjidetail;

import java.util.List;
import java.util.Objects;
import javax.swing.table.AbstractTableModel;
import me.johannesschaeufele.kanjifocus.database.data.Kanji;
import me.johannesschaeufele.kanjifocus.database.data.Nanori;
import me.johannesschaeufele.kanjifocus.database.data.Reading;
import me.johannesschaeufele.kanjifocus.database.user.Progress;
import me.johannesschaeufele.kanjifocus.gui.FocusModel;
import me.johannesschaeufele.kanjifocus.gui.ValuePair;

public class KanjiDetailTableModel extends AbstractTableModel {

    private final FocusModel focusModel;

    private String kanji;
    private Kanji k;

    public KanjiDetailTableModel(FocusModel focusModel) {
        this.focusModel = focusModel;
    }

    private synchronized void checkValidity() {
        if(!Objects.equals(this.kanji, this.focusModel.getKanji())) {
            this.kanji = this.focusModel.getKanji();

            if(this.kanji == null) {
                this.k = null;
            }
            else {
                this.k = this.focusModel.getDataDatabase().getKanji(this.kanji);
            }
        }
    }

    public void changeProgress(int row, int column, int value) {
        this.checkValidity();

        if(this.kanji == null) {
            return;
        }

        if(row <= 1) {
            if(column <= 1) {
                Progress[] kanjiProgress = this.focusModel.getUserDatabase().getKanjiProgress(this.kanji);

                if(column == 0) {
                    if((row & 1) == 0) {
                        kanjiProgress[0] = Progress.byId[Math.max(0, Math.min(Progress.byId.length - 1, kanjiProgress[0].getId() + value))];
                    }
                    else {
                        kanjiProgress[1] = Progress.byId[Math.max(0, Math.min(Progress.byId.length - 1, kanjiProgress[1].getId() + value))];
                    }
                }
                else {
                    if((row & 1) == 0) {
                        kanjiProgress[2] = Progress.byId[Math.max(0, Math.min(Progress.byId.length - 1, kanjiProgress[2].getId() + value))];
                    }
                    else {
                        kanjiProgress[3] = Progress.byId[Math.max(0, Math.min(Progress.byId.length - 1, kanjiProgress[3].getId() + value))];
                    }
                }

                this.focusModel.getUserDatabase().setKanjiProgress(this.kanji, kanjiProgress);
            }
            else {
                List<Reading> readings;

                if((row & 1) == 0) {
                    readings = this.k.getOnyomi();
                }
                else {
                    readings = this.k.getKunyomi();
                }

                if(column - 2 < readings.size()) {
                    Reading reading = readings.get(column - 2);

                    Progress progress = this.focusModel.getUserDatabase().getReadingProgress(reading.getId());
                    Progress changed = Progress.byId[Math.max(0, Math.min(Progress.byId.length - 1, progress.getId() + value))];

                    this.focusModel.getUserDatabase().setReadingProgress(reading.getId(), changed);
                }
            }
        }
        else if(row == 3) {
            List<Nanori> nanori = this.k.getNanori();

            if(column >= 2 && column - 2 < nanori.size()) {
                Nanori n = nanori.get(column - 2);

                Progress progress = this.focusModel.getUserDatabase().getNanoriProgress(n.getId());
                Progress changed = Progress.byId[Math.max(0, Math.min(Progress.byId.length - 1, progress.getId() + value))];

                this.focusModel.getUserDatabase().setNanoriProgress(n.getId(), changed);
            }
        }

        this.fireTableCellUpdated(row, column);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return ValuePair.class;
    }

    @Override
    public int getColumnCount() {
        return 1 + 1 + 18;
    }

    @Override
    public int getRowCount() {
        return 4;
    }

    @Override
    public ValuePair getValueAt(int row, int column) {
        this.checkValidity();

        if(this.kanji == null) {
            return null;
        }

        if(row <= 1) {
            if(column <= 1) {
                Progress[] kanjiProgress = this.focusModel.getUserDatabase().getKanjiProgress(this.kanji);

                if(column == 0) {
                    if((row & 1) == 0) {
                        return new ValuePair(this.kanji, kanjiProgress[0]);
                    }
                    else {
                        return new ValuePair("m", kanjiProgress[1]);
                    }
                }
                else {
                    if((row & 1) == 0) {
                        return new ValuePair("R", kanjiProgress[2]);
                    }
                    else {
                        return new ValuePair("W", kanjiProgress[3]);
                    }
                }
            }
            else {
                List<Reading> readings;

                if((row & 1) == 0) {
                    readings = this.k.getOnyomi();
                }
                else {
                    readings = this.k.getKunyomi();
                }

                if(column - 2 < readings.size()) {
                    Reading reading = readings.get(column - 2);

                    return new ValuePair(reading.getReading(), this.focusModel.getUserDatabase().getReadingProgress(reading.getId()));
                }
            }
        }
        else if(row == 3) {
            List<Nanori> nanori = this.k.getNanori();

            if(column >= 2 && column - 2 < nanori.size()) {
                Nanori n = nanori.get(column - 2);

                return new ValuePair(n.getNanori(), this.focusModel.getUserDatabase().getNanoriProgress(n.getId()));
            }
        }

        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void update() {
        this.fireTableDataChanged();
    }

}
