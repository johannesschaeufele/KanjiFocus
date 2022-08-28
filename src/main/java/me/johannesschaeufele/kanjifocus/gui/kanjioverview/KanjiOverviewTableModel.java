package me.johannesschaeufele.kanjifocus.gui.kanjioverview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import me.johannesschaeufele.kanjifocus.database.data.Kanji;
import me.johannesschaeufele.kanjifocus.database.data.Reading;
import me.johannesschaeufele.kanjifocus.database.user.Progress;
import me.johannesschaeufele.kanjifocus.gui.FocusModel;
import me.johannesschaeufele.kanjifocus.gui.TableRowCache;
import me.johannesschaeufele.kanjifocus.gui.ValuePair;

public class KanjiOverviewTableModel extends AbstractTableModel {

    private static final int CACHE_ROW_COUNT = 200;

    private final FocusModel focusModel;

    private List<String> kanji = Collections.emptyList();
    private final TableRowCache<Kanji> kanjiCache = new TableRowCache<>(Kanji.class);
    private boolean kanjiValid = false;

    private List<String> tagsInclude = null;
    private List<String> tagsExclude = null;
    private List<String> readingSearch = null;
    private List<Progress> progressFilter = null;

    private boolean shuffle = false;

    private int learnMode = 0;
    private boolean displayOn = true;
    private boolean displayKun = true;

    public KanjiOverviewTableModel(FocusModel focusModel) {
        this.focusModel = focusModel;

        this.kanjiCache.initialize(CACHE_ROW_COUNT);
    }

    private synchronized void checkValidity() {
        if(!this.kanjiValid) {
            this.kanji = this.focusModel.getDataDatabase().getKanjiList(this.tagsInclude, this.tagsExclude, this.readingSearch);
            this.kanji = this.focusModel.getUserDatabase().filterKanjiProgress(this.kanji, this.progressFilter);

            if(this.shuffle) {
                Collections.shuffle(this.kanji);
            }

            this.kanjiCache.initialize(CACHE_ROW_COUNT);

            this.kanjiValid = true;
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return ValuePair.class;
    }

    @Override
    public int getColumnCount() {
        return 1 + 1 + 18 + 1;
    }

    public String getKanji(int kanjiRow) {
        this.checkValidity();

        return this.kanji.get(kanjiRow);
    }

    @Override
    public int getRowCount() {
        this.checkValidity();

        return this.kanji.size() * 2;
    }

    @Override
    public ValuePair getValueAt(int row, int column) {
        this.checkValidity();

        int kanjiRow = row / 2;

        Kanji k = this.kanjiCache.get(kanjiRow);

        if(k == null) {
            int batch_size = Math.max(Math.min(CACHE_ROW_COUNT / 2, 20), 1);

            int startRow = Math.max(kanjiRow - batch_size / 2, 0);
            while(startRow < kanjiRow && this.kanjiCache.hasCacheRow(startRow) && this.kanjiCache.get(startRow) != null) {
                startRow++;
            }
            int endRow = Math.min(startRow + batch_size, this.kanji.size() - 1);
            while(endRow > kanjiRow && this.kanjiCache.hasCacheRow(endRow) && this.kanjiCache.get(endRow) != null) {
                endRow--;
            }

            List<String> kanjiBatch = new ArrayList<>(endRow - startRow + 1);
            for(int batchRow = startRow; batchRow <= endRow; batchRow++) {
                kanjiBatch.add(this.kanji.get(batchRow));
            }

            List<Kanji> resultBatch = this.focusModel.getDataDatabase().getKanjiBatched(kanjiBatch);

            for(int batchRow = startRow; batchRow <= endRow; batchRow++) {
                this.kanjiCache.set(batchRow, resultBatch.get(batchRow - startRow));
            }

            k = resultBatch.get(kanjiRow - startRow);
        }

        if(column == 0) {
            if(this.learnMode > 1) {
                return new ValuePair("", null);
            }

            Progress[] kanjiProgress = this.focusModel.getUserDatabase().getKanjiProgress(k.getKanji());

            if(this.learnMode != 0) {
                kanjiProgress[0] = null;
            }

            return new ValuePair(k.getKanji(), kanjiProgress[0]);
        }
        else if(column == 1) {
            Progress[] kanjiProgress = this.focusModel.getUserDatabase().getKanjiProgress(k.getKanji());

            if(this.learnMode != 0) {
                kanjiProgress[2] = null;
                kanjiProgress[3] = null;
            }

            if((row & 1) == 0) {
                return new ValuePair("R", kanjiProgress[2]);
            }
            else {
                return new ValuePair("W", kanjiProgress[3]);
            }
        }
        else if(column == this.getColumnCount() - 1 && (row & 1) == 1) {
            if(this.learnMode == 0 || this.learnMode == 3) {
                return new ValuePair(k.getMeanings(), null);
            }
        }
        else if(column > 1) {
            if((this.learnMode & 1) != 0) {
                return null;
            }

            List<Reading> readings;

            if((row & 1) == 0) {
                if(!this.displayOn) {
                    return null;
                }

                readings = k.getOnyomi();
            }
            else {
                if(!this.displayKun) {
                    return null;
                }

                readings = k.getKunyomi();
            }

            if(column - 2 < readings.size()) {
                Reading reading = readings.get(column - 2);

                Progress progress = this.focusModel.getUserDatabase().getReadingProgress(reading.getId());

                if(this.learnMode == 2) {
                    progress = null;
                }

                return new ValuePair(reading.getReading(), progress);
            }
        }

        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void setLearnMode(int learnMode) {
        this.learnMode = learnMode;

        this.fireTableDataChanged();
    }

    public void setProgressFilter(List<Progress> progressFilter) {
        this.kanjiValid = false;

        this.progressFilter = progressFilter;

        this.fireTableDataChanged();
    }

    public void setReadingSearch(List<String> readingSearch) {
        this.kanjiValid = false;

        this.readingSearch = readingSearch;

        this.fireTableDataChanged();
    }

    public void setTags(List<String> tagsInclude, List<String> tagsExclude) {
        this.kanjiValid = false;

        this.tagsInclude = tagsInclude;
        this.tagsExclude = tagsExclude;

        this.fireTableDataChanged();
    }

    public void setShuffe(boolean shuffle) {
        this.kanjiValid = false;

        this.shuffle = shuffle;

        this.fireTableDataChanged();
    }

    public void setDisplayOn(boolean displayOn) {
        this.displayOn = displayOn;

        this.fireTableDataChanged();
    }

    public void setDisplayKun(boolean displayKun) {
        this.displayKun = displayKun;

        this.fireTableDataChanged();
    }

    public void updateProgress() {
        this.fireTableDataChanged();
    }

}
