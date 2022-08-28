package me.johannesschaeufele.kanjifocus.configuration;

/**
 * Serializable class that stores user preferences and settings
 * Configuration values are maintained between subsequent runs of the application
 */
public class Configuration {

    /** Currently active tab */
    private int selectedTab = 0;

    private int[] kanjiOverviewProgress = new int[]{};

    /** Kanji overview tag search */
    private String kanjiOverviewTags = "";

    /** Kanji overview textual search */
    private String kanjiOverviewSearch = "";

    /** Whether to ignore tag search */
    private boolean kanjiOverviewSearchAll = false;

    /** Whether to shuffle the order of kanji */
    private boolean kanjiOverviewShuffle = false;

    private int kanjiOverviewLearnMode = 0;

    private int kanjiOverviewScroll = 0;

    /** Selected kanji to view the details of */
    private String kanjiDetailKanji = null;

    /** Kanji detail vocabulary tag search */
    private String kanjiDetailVocabularyTags = "";

    public Configuration() {
    }

    public String getKanjiDetailKanji() {
        return this.kanjiDetailKanji;
    }

    public String getKanjiDetailVocabularyTags() {
        return this.kanjiDetailVocabularyTags;
    }

    public int getKanjiOverviewLearnMode() {
        return this.kanjiOverviewLearnMode;
    }

    public int[] getKanjiOverviewProgress() {
        return this.kanjiOverviewProgress;
    }

    public int getKanjiOverviewScroll() {
        return this.kanjiOverviewScroll;
    }

    public String getKanjiOverviewSearch() {
        return this.kanjiOverviewSearch;
    }

    public String getKanjiOverviewTags() {
        return this.kanjiOverviewTags;
    }

    public int getSelectedTab() {
        return this.selectedTab;
    }

    public void setKanjiDetailKanji(String kanjiDetailKanji) {
        this.kanjiDetailKanji = kanjiDetailKanji;
    }

    public void setKanjiDetailVocabularyTags(String kanjiDetailVocabularyTags) {
        this.kanjiDetailVocabularyTags = kanjiDetailVocabularyTags;
    }

    public void setKanjiOverviewLearnMode(int kanjiOverviewLearnMode) {
        this.kanjiOverviewLearnMode = kanjiOverviewLearnMode;
    }

    public void setKanjiOverviewProgress(int[] kanjiOverviewProgress) {
        this.kanjiOverviewProgress = kanjiOverviewProgress;
    }

    public void setKanjiOverviewScroll(int kanjiOverviewScroll) {
        this.kanjiOverviewScroll = kanjiOverviewScroll;
    }

    public void setKanjiOverviewSearch(String kanjiOverviewSearch) {
        this.kanjiOverviewSearch = kanjiOverviewSearch;
    }

    public void setKanjiOverviewSearchAll(boolean kanjiOverviewSearchAll) {
        this.kanjiOverviewSearchAll = kanjiOverviewSearchAll;
    }

    public void setKanjiOverviewShuffle(boolean kanjiOverviewShuffle) {
        this.kanjiOverviewShuffle = kanjiOverviewShuffle;
    }

    public void setKanjiOverviewTags(String kanjiOverviewTags) {
        this.kanjiOverviewTags = kanjiOverviewTags;
    }

    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

}
