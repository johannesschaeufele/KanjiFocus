package me.johannesschaeufele.kanjifocus.database.data;

import java.util.List;

/**
 * Serializable class containing information for a single kanji
 * Once loaded from the database, this information is treated as read-only
 */
public class Kanji {

    /** Unique kanji id */
    private final int id;
    /** String containing the kanji character */
    private final String kanji;
    /** ID of the primary (dictionary) radical for this kanji */
    private final int radical;
    /** List of all radical characters for this kanji */
    private final List<String> radicals;
    /** Canonical number of strokes used to draw this kanji */
    private final int strokeCount;
    /** Meanings of the kanji, aggregated into a single string */
    private final String meanings;

    /** List of the onyomi (chinese / phonetic readings) of this kanji */
    private final List<Reading> onyomi;
    /** List of the kunyomi (japanese / meaning readings) of this kanji */
    private final List<Reading> kunyomi;

    /** List of the nanori (name readings) of this kanji */
    private final List<Nanori> nanori;

    /** List of tags this kanji is tagged with */
    private final List<String> tags;

    public Kanji(int id, String kanji, int radical, List<String> radicals, int strokeCount, String meanings, List<Reading> onyomi, List<Reading> kunyomi, List<Nanori> nanori, List<String> tags) {
        this.id = id;
        this.kanji = kanji;
        this.radical = radical;
        this.radicals = radicals;
        this.strokeCount = strokeCount;
        this.meanings = meanings;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        this.nanori = nanori;
        this.tags = tags;
    }

    public int getId() {
        return this.id;
    }

    public String getKanji() {
        return this.kanji;
    }

    public List<Reading> getKunyomi() {
        return this.kunyomi;
    }

    public String getMeanings() {
        return this.meanings;
    }

    public List<Nanori> getNanori() {
        return this.nanori;
    }

    public List<Reading> getOnyomi() {
        return this.onyomi;
    }

    public int getRadical() {
        return this.radical;
    }

    public List<String> getRadicals() {
        return this.radicals;
    }

    public int getStrokeCount() {
        return this.strokeCount;
    }

    public List<String> getTags() {
        return this.tags;
    }

}
