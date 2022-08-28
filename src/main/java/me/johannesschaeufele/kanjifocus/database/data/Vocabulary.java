package me.johannesschaeufele.kanjifocus.database.data;

import java.util.List;

public class Vocabulary {

    /** Unique vocabulary id */
    private final int id;
    /** String representation of the vocabulary, potentially containing kanji, kana, and punctuation */
    private final String vocabulary;
    /** Kana representation of the vocabulary, containing kana and punctuation */
    private final String kana;

    /** List of senses for this vocabulary */
    private final List<Sense> senses;
    /** List of tags that this vocabulary is tagged with */
    private final List<String> categories;

    public Vocabulary(int id, String vocabulary, String kana, List<Sense> senses, List<String> categories) {
        this.id = id;
        this.vocabulary = vocabulary;
        this.kana = kana;
        this.senses = senses;
        this.categories = categories;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public int getId() {
        return this.id;
    }

    public String getKana() {
        return this.kana;
    }

    public List<Sense> getSenses() {
        return this.senses;
    }

    public String getVocabulary() {
        return this.vocabulary;
    }

}
