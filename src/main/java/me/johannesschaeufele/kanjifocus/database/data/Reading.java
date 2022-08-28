package me.johannesschaeufele.kanjifocus.database.data;

/**
 * Serializable read-only class containing the information for a reading (onyomi / kunyomi) of a kanji
 */
public class Reading {

    /** Unique reading id */
    private final int id;
    /** Kana + punctuation string of the reading */
    private final String reading;

    public Reading(int id, String reading) {
        this.id = id;
        this.reading = reading;
    }

    public int getId() {
        return this.id;
    }

    public String getReading() {
        return this.reading;
    }

}
