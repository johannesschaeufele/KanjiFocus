package me.johannesschaeufele.kanjifocus.database.data;

/**
 * Serializable read-only class containing the information for a name reading of a kanji
 */
public class Nanori {

    /** Unique nanori id */
    private final int id;
    /** Kana + punctuation string of the reading */
    private final String nanori;

    public Nanori(int id, String nanori) {
        this.id = id;
        this.nanori = nanori;
    }

    public int getId() {
        return this.id;
    }

    public String getNanori() {
        return this.nanori;
    }

}
