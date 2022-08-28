package me.johannesschaeufele.kanjifocus.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import me.johannesschaeufele.kanjifocus.database.data.DataDatabase.VocabularyReading;
import me.johannesschaeufele.kanjifocus.database.data.Sense;
import me.johannesschaeufele.kanjifocus.database.data.Vocabulary;
import me.johannesschaeufele.kanjifocus.database.user.Progress;
import me.johannesschaeufele.kanjifocus.util.ReadingUtil;

public class VocabularyTableModel extends AbstractTableModel {

    private static final int CACHE_ROW_COUNT = 100;

    private final FocusModel focusModel;

    private List<String> tags = null;
    private List<String> readingSearch = null;
    private List<String> mustIncludes = null;
    private List<String> searchTerms = null;
    private List<Progress> progressFilter = null;

    private String kanji = null;
    private String reading = null;

    private List<Integer> vocabulary = Collections.emptyList();

    private final TableRowCache<Vocabulary> vocabularyCache = new TableRowCache<>(Vocabulary.class);

    private boolean vocabularyValid = false;

    public VocabularyTableModel(FocusModel focusModel) {
        this.focusModel = focusModel;

        this.vocabularyCache.initialize(CACHE_ROW_COUNT);
    }

    private synchronized void checkValidity() {
        if(!this.vocabularyValid) {
            List<VocabularyReading> vocabularyReadings = this.focusModel.getDataDatabase().getVocabularyReadings(this.tags, this.mustIncludes, this.searchTerms, this.readingSearch);
            this.vocabulary = new ArrayList<>(vocabularyReadings.size());

            for(VocabularyReading vr : vocabularyReadings) {
                if(this.reading == null || ReadingUtil.usesReading(vr.getVocabulary(), vr.getKana(), this.kanji, this.reading)) {
                    this.vocabulary.add(vr.getVocabularyId());
                }
            }

            this.vocabulary = this.focusModel.getUserDatabase().filterVocabularyProgress(this.vocabulary, this.progressFilter);

            this.vocabularyCache.initialize(CACHE_ROW_COUNT);

            this.vocabularyValid = true;
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return ValuePair.class;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    public int getVocabulary(int vocabularyRow) {
        this.checkValidity();

        return this.vocabulary.get(vocabularyRow);
    }

    @Override
    public int getRowCount() {
        this.checkValidity();

        return this.vocabulary.size();
    }

    @Override
    public ValuePair getValueAt(int row, int column) {
        this.checkValidity();

        Vocabulary v = this.vocabularyCache.get(row);

        if(v == null) {
            v = this.focusModel.getDataDatabase().getVocabulary(this.vocabulary.get(row));

            this.vocabularyCache.set(row, v);
        }

        Progress progress = this.focusModel.getUserDatabase().getVocabularyProgress(v.getId());

        if(column == 0) {
            return new ValuePair(v.getVocabulary() + " (" + v.getKana() + ")", progress);
        }
        else if(column == 1) {
            List<Sense> senses = v.getSenses();

            String s = "";
            boolean first = true;
            for(Sense sense : senses) {
                if(first) {
                    first = false;
                }
                else {
                    s += "; ";
                }

                s += sense.getSense();
            }

            return new ValuePair(s, progress);
        }

        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void setProgressFilter(List<Progress> progressFilter) {
        this.vocabularyValid = false;

        this.progressFilter = progressFilter;

        this.fireTableDataChanged();
    }

    public void setReadingSearch(List<String> readingSearch) {
        this.vocabularyValid = false;

        this.readingSearch = readingSearch;

        this.fireTableDataChanged();
    }

    public void setTags(List<String> tags) {
        this.vocabularyValid = false;

        this.tags = tags;

        this.fireTableDataChanged();
    }

    public void setMustIncludes(List<String> mustIncludes) {
        this.vocabularyValid = false;

        this.mustIncludes = mustIncludes;

        this.fireTableDataChanged();
    }

    public void setSearchTerms(List<String> searchTerms) {
        this.vocabularyValid = false;

        this.searchTerms = searchTerms;

        this.fireTableDataChanged();
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public void setReading(String reading) {
        this.vocabularyValid = false;

        this.reading = reading;

        this.fireTableDataChanged();
    }

    public void updateProgress() {
        this.fireTableDataChanged();
    }

}
