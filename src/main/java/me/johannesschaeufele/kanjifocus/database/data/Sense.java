package me.johannesschaeufele.kanjifocus.database.data;

/**
 * Serializable read-only class containing the information for a sense of vocabulary
 *
 * A sense is akin to one of multiple entries for vocabulary in a dictionary, and typically
 * applies to a specific form or usage of the vocabulary
 */
public class Sense {

    /** Unique sense id */
    private final int id;
    /** String description of the sense */
    private final String sense;

    public Sense(int id, String sense) {
        this.id = id;
        this.sense = sense;
    }

    public int getId() {
        return this.id;
    }

    public String getSense() {
        return this.sense;
    }

}
