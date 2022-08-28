package me.johannesschaeufele.kanjifocus.database.data;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.johannesschaeufele.kanjifocus.database.Database;

/**
 * This class allows querying of the data database containing read-only information on kanji, readings, vocabulary, and the like
 */
public class DataDatabase extends Database {

    public DataDatabase(File file) {
        super(file);
    }

    @Override
    public void initialize() {
        try {
            this.connect(true);
        }
        catch(SQLException ex) {
            Logger.getLogger(DataDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getKanjiList(List<String> tagsInclude, List<String> tagsExclude, List<String> readingSearch) {
        try {
            List<String> kanji = new ArrayList<>();

            PreparedStatement statementSearch;
            if(tagsInclude == null && tagsExclude == null && readingSearch == null) {
                statementSearch = this.connection.prepareStatement(
                        "SELECT DISTINCT id "
                                + "FROM kanji "
                                + "ORDER BY order_index ASC"
                );
            }
            else if(readingSearch == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT id FROM ("
                        + "SELECT DISTINCT k.id AS id, k.order_index AS order_index "
                        + "FROM "
                );

                if(tagsInclude == null) {
                    sb.append("kanji AS k ");
                }
                else {
                    sb.append("kanji_tags AS kt, tags AS t, kanji AS k "
                            + "WHERE kt.kanji = k.id AND kt.tag = t.id "
                            + "AND t.name IN (?");
                    for(int i = 1; i < tagsInclude.size(); i++) {
                        sb.append(", ?");
                    }
                    sb.append(") ");
                }

                if(tagsExclude != null) {
                    sb.append("EXCEPT "
                            + "SELECT DISTINCT k.id AS id, k.order_index AS order_index "
                            + "FROM kanji_tags AS kt, tags AS t, kanji AS k "
                            + "WHERE kt.kanji = k.id AND kt.tag = t.id "
                    );

                    sb.append("AND t.name IN (?");
                    for(int i = 1; i < tagsExclude.size(); i++) {
                        sb.append(", ?");
                    }
                    sb.append(") ");
                }

                sb.append(") "
                        + "ORDER BY order_index ASC");

                statementSearch = this.connection.prepareStatement(sb.toString());

                int j = 1;
                if(tagsInclude != null) {
                    for(int i = 0; i < tagsInclude.size(); i++) {
                        statementSearch.setString(j++, tagsInclude.get(i));
                    }
                }
                if(tagsExclude != null) {
                    for(int i = 0; i < tagsExclude.size(); i++) {
                        statementSearch.setString(j++, tagsExclude.get(i));
                    }
                }
            }
            /*else if(tags == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT DISTINCT k.id AS id "
                        + "FROM ("
                );

                sb.append("SELECT DISTINCT kanji AS id "
                        + "FROM readings "
                        + "WHERE romaji LIKE ? "
                );
                for(int i = 1; i < readingSearch.size(); i++) {
                    sb.append(" UNION ALL SELECT DISTINCT kanji AS id "
                            + "FROM readings "
                            + "WHERE romaji LIKE ? "
                    );
                }

                sb.append(") AS rs, kanji AS k "
                        + "WHERE rs.id = k.id "
                        + "GROUP BY k.id "
                        + "ORDER BY COUNT(*) DESC, k.order_index ASC"
                );

                statementSearch = this.connection.prepareStatement(sb.toString());

                for(int i = 0; i < readingSearch.size(); i++) {
                    statementSearch.setString(i + 1, readingSearch.get(i) + "%");
                }
            }*/
            else {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT DISTINCT k.id AS id "
                        + "FROM ("
                );

                sb.append("SELECT DISTINCT kanji AS id "
                        + "FROM readings "
                        + "WHERE romaji LIKE ? "
                );
                for(int i = 1; i < readingSearch.size(); i++) {
                    sb.append(" UNION ALL SELECT DISTINCT kanji AS id "
                            + "FROM readings "
                            + "WHERE romaji LIKE ? "
                    );
                }

                if(tagsInclude == null) {
                    sb.append(") AS rs, kanji AS k "
                            + "WHERE rs.id = k.id "
                    );
                }
                else {
                    sb.append(") AS rs, kanji AS k, kanji_tags AS kt, tags AS t "
                            + "WHERE rs.id = k.id AND kt.kanji = k.id AND kt.tag = t.id AND t.name IN (?"
                    );

                    for(int i = 1; i < tagsInclude.size(); i++) {
                        sb.append(", ?");
                    }

                    sb.append(") ");
                }
                sb.append("GROUP BY k.id "
                        + "ORDER BY COUNT(*) DESC, k.order_index ASC"
                );

                statementSearch = this.connection.prepareStatement(sb.toString());

                for(int i = 0; i < readingSearch.size(); i++) {
                    statementSearch.setString(i + 1, readingSearch.get(i) + "%");
                }

                if(tagsInclude != null) {
                    for(int i = 0; i < tagsInclude.size(); i++) {
                        statementSearch.setString(readingSearch.size() + i + 1, tagsInclude.get(i));
                    }
                }
            }

            statementSearch.setFetchSize(10000);
            ResultSet result = statementSearch.executeQuery();

            while(result.next()) {
                kanji.add(new String(new int[]{result.getInt("id")}, 0, 1));
            }

            return kanji;
        }
        catch(SQLException ex) {
            Logger.getLogger(DataDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Kanji> getKanjiBatched(List<String> kanjiList) {
        List<Kanji> results = new ArrayList<>(kanjiList.size());

        try {
            PreparedStatement statementKanji = this.connection.prepareStatement(
                    "SELECT radical, stroke_count, meanings "
                            + "FROM kanji "
                            + "WHERE id = ? "
                            + "ORDER BY order_index ASC"
            );

            List<Integer> ids = new ArrayList<>(kanjiList.size());
            for(String kanji : kanjiList) {
                int id = Character.codePointAt(kanji, 0);

                ids.add(id);
            }

            List<Integer> radicalList = new ArrayList<>(kanjiList.size());
            List<Integer> strokeCountList = new ArrayList<>(kanjiList.size());
            List<String> meaningsList = new ArrayList<>(kanjiList.size());
            for(Integer id : ids) {
                statementKanji.setInt(1, id);
                ResultSet resultKanji = statementKanji.executeQuery();

                int radical = -1;
                int strokeCount = 0;
                String meanings = "";
                if(resultKanji.next()) {
                    radical = resultKanji.getInt("radical");
                    strokeCount = resultKanji.getInt("stroke_count");
                    meanings = resultKanji.getString("meanings");
                }

                radicalList.add(radical);
                strokeCountList.add(strokeCount);
                meaningsList.add(meanings);
            }

            PreparedStatement statementKanjiRadicals = this.connection.prepareStatement(
                    "SELECT radical "
                            + "FROM radicals "
                            + "WHERE kanji = ? "
                            + "ORDER BY id ASC"
            );
            statementKanjiRadicals.setFetchSize(20);

            List<List<String>> radicalsList = new ArrayList<>(kanjiList.size());
            for(Integer id : ids) {
                statementKanjiRadicals.setInt(1, id);
                ResultSet resultKanjiRadicals = statementKanjiRadicals.executeQuery();

                List<String> radicals = new ArrayList<>();
                while(resultKanjiRadicals.next()) {
                    radicals.add(new String(new int[]{resultKanjiRadicals.getInt("radical")}, 0, 1));
                }

                radicalsList.add(radicals);
            }

            PreparedStatement statementReadings = this.connection.prepareStatement(
                    "SELECT id, type, reading "
                            + "FROM readings "
                            + "WHERE kanji = ? "
                            + "ORDER BY id ASC"
            );
            statementReadings.setFetchSize(40);

            List<List<Reading>> onyomiList = new ArrayList<>(kanjiList.size());
            List<List<Reading>> kunyomiList = new ArrayList<>(kanjiList.size());
            for(Integer id : ids) {
                statementReadings.setInt(1, id);
                ResultSet resultReadings = statementReadings.executeQuery();

                List<Reading> onyomi = new ArrayList<>();
                List<Reading> kunyomi = new ArrayList<>();
                while(resultReadings.next()) {
                    int rId = resultReadings.getInt("id");
                    String reading = resultReadings.getString("reading");

                    Reading r = new Reading(rId, reading);

                    if(resultReadings.getBoolean("type")) {
                        kunyomi.add(r);
                    }
                    else {
                        onyomi.add(r);
                    }
                }

                onyomiList.add(onyomi);
                kunyomiList.add(kunyomi);
            }

            PreparedStatement statementNanori = this.connection.prepareStatement(
                    "SELECT id, nanori "
                            + "FROM nanori "
                            + "WHERE kanji = ? "
                            + "ORDER BY id ASC"
            );
            statementNanori.setFetchSize(20);

            List<List<Nanori>> nanoriList = new ArrayList<>(kanjiList.size());
            for(Integer id : ids) {
                statementNanori.setInt(1, id);

                ResultSet resultNanori = statementNanori.executeQuery();

                List<Nanori> nanori = new ArrayList<>();
                while(resultNanori.next()) {
                    nanori.add(new Nanori(resultNanori.getInt("id"), resultNanori.getString("nanori")));
                }
                nanoriList.add(nanori);
            }

            PreparedStatement statementTags = this.connection.prepareStatement(
                    "SELECT t.name AS tag_name "
                            + "FROM kanji_tags AS kt, tags AS t "
                            + "WHERE kt.kanji = ? AND kt.tag = t.id "
                            + "ORDER BY t.id ASC"
            );

            List<List<String>> tagsList = new ArrayList<>(kanjiList.size());
            for(Integer id : ids) {
                statementTags.setInt(1, id);

                ResultSet resultTags = statementTags.executeQuery();

                List<String> tags = new ArrayList<>();
                while(resultTags.next()) {
                    tags.add(resultTags.getString("tag_name"));
                }

                tagsList.add(tags);
            }

            for(int i = 0; i < kanjiList.size(); i++) {
                results.add(
                        new Kanji(ids.get(i), kanjiList.get(i), radicalList.get(i), radicalsList.get(i),
                                strokeCountList.get(i), meaningsList.get(i), onyomiList.get(i), kunyomiList.get(i),
                                nanoriList.get(i), tagsList.get(i))
                );
            }
        }
        catch(SQLException ex) {
            Logger.getLogger(DataDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return results;
    }

    public Kanji getKanji(String kanji) {
        List<Kanji> kanjiList = this.getKanjiBatched(Arrays.asList(kanji));

        if(!kanji.isEmpty()) {
            return kanjiList.get(0);
        }
        else {
            return null;
        }
    }

    public static class VocabularyReading {

        private final int vocabularyId;
        private final String vocabulary;
        private final String kana;

        public VocabularyReading(int vocabularyId, String vocabulary, String kana) {
            this.vocabularyId = vocabularyId;
            this.vocabulary = vocabulary;
            this.kana = kana;
        }

        public String getKana() {
            return kana;
        }

        public String getVocabulary() {
            return vocabulary;
        }

        public int getVocabularyId() {
            return vocabularyId;
        }

    }

    public List<VocabularyReading> getVocabularyReadings(List<String> categories, List<String> mustIncludes, List<String> searchTerms, List<String> readingSearch) {
        if(categories == null) {
            categories = Collections.emptyList();
        }
        if(mustIncludes == null) {
            mustIncludes = Collections.emptyList();
        }
        if(searchTerms == null) {
            searchTerms = Collections.emptyList();
        }
        if(readingSearch == null) {
            readingSearch = Collections.emptyList();
        }

        try {
            List<VocabularyReading> vocabularyReadings = new ArrayList<>();

            boolean usesCategories = !categories.isEmpty();
            boolean usesMustIncludes = !mustIncludes.isEmpty();
            boolean usesSearchTerms = !searchTerms.isEmpty();
            boolean usesReadingSearch = !readingSearch.isEmpty();
            boolean usesAny = usesCategories || usesMustIncludes || usesSearchTerms || usesReadingSearch;

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT v.id, v.vocabulary, v.kana "
                    + "FROM vocabulary AS v");

            if(usesCategories) {
                sb.append(", categories AS c, vocabulary_categories AS vc");
            }

            sb.append(" ");

            if(usesAny) {
                sb.append("WHERE ");

                if(usesCategories) {
                    sb.append("vc.vocabulary = v.id AND vc.category = c.id AND c.name IN (?");

                    for(int i = 1; i < categories.size(); i++) {
                        sb.append(", ?");
                    }

                    sb.append(")");
                }
                if(usesMustIncludes) {
                    if(usesCategories) {
                        sb.append(" AND ");
                    }

                    sb.append("v.vocabulary LIKE ?");

                    for(int i = 1; i < mustIncludes.size(); i++) {
                        sb.append(" AND v.vocabulary LIKE ?");
                    }
                }
                if(usesSearchTerms) {
                    if(usesCategories || usesMustIncludes) {
                        sb.append(" AND ");
                    }

                    sb.append("v.romaji LIKE ?");

                    for(int i = 1; i < searchTerms.size(); i++) {
                        sb.append(" AND v.romaji LIKE ?");
                    }
                }
                if(usesReadingSearch) {
                    if(usesCategories || usesMustIncludes || usesSearchTerms) {
                        sb.append(" AND ");
                    }

                    sb.append("(v.romaji LIKE ?");

                    for(int i = 1; i < readingSearch.size(); i++) {
                        sb.append(" OR v.romaji LIKE ?");
                    }

                    sb.append(")");
                }
                sb.append(" ");
            }

            sb.append("ORDER BY order_index ASC");

            PreparedStatement statementSearch = this.connection.prepareStatement(sb.toString());

            int index = 1;
            for(String category : categories) {
                statementSearch.setString(index++, category);
            }
            for(String mustInclude : mustIncludes) {
                statementSearch.setString(index++, "%" + mustInclude + "%");
            }
            for(String searchTerm : searchTerms) {
                statementSearch.setString(index++, "%" + searchTerm + "%");
            }
            for(String rs : readingSearch) {
                statementSearch.setString(index++, rs + "%");
            }

            ResultSet result = statementSearch.executeQuery();

            while(result.next()) {
                vocabularyReadings.add(new VocabularyReading(result.getInt("id"), result.getString("vocabulary"), result.getString("kana")));
            }

            return vocabularyReadings;
        }
        catch(SQLException ex) {
            Logger.getLogger(DataDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Integer> getVocabularyList(List<String> categories, List<String> mustIncludes, List<String> searchTerms, List<String> readingSearch) {
        List<VocabularyReading> vocabularyReadings = this.getVocabularyReadings(categories, mustIncludes, searchTerms, readingSearch);
        List<Integer> vocabulary = new ArrayList<>(vocabularyReadings.size());

        for(VocabularyReading vocabularyReading : vocabularyReadings) {
            vocabulary.add(vocabularyReading.getVocabularyId());
        }

        return vocabulary;
    }

    public Vocabulary getVocabulary(int vocabularyId) {
        try {
            PreparedStatement statementVocabulary = this.connection.prepareStatement(
                    "SELECT vocabulary, kana "
                            + "FROM vocabulary "
                            + "WHERE id = ? "
                            + "ORDER BY order_index ASC"
            );

            statementVocabulary.setInt(1, vocabularyId);
            statementVocabulary.setFetchSize(10000);
            ResultSet resultVocabulary = statementVocabulary.executeQuery();

            if(resultVocabulary.next()) {
                String vocabulary = resultVocabulary.getString("vocabulary");
                String kana = resultVocabulary.getString("kana");

                PreparedStatement statementSenses = this.connection.prepareStatement(
                        "SELECT id, sense "
                                + "FROM senses "
                                + "WHERE vocabulary = ? "
                                + "ORDER BY id ASC"
                );

                statementSenses.setInt(1, vocabularyId);

                PreparedStatement statementCategories = this.connection.prepareStatement(
                        "SELECT c.name AS category_name "
                                + "FROM vocabulary_categories AS vc, categories AS c "
                                + "WHERE vc.vocabulary = ? AND vc.category = c.id "
                                + "ORDER BY c.id ASC"
                );

                statementCategories.setInt(1, vocabularyId);

                statementSenses.setFetchSize(30);
                ResultSet resultSenses = statementSenses.executeQuery();

                ResultSet resultCategories = statementCategories.executeQuery();

                List<Sense> senses = new ArrayList<>();
                while(resultSenses.next()) {
                    senses.add(new Sense(resultSenses.getInt("id"), resultSenses.getString("sense")));
                }

                List<String> categories = new ArrayList<>();
                while(resultCategories.next()) {
                    categories.add(resultCategories.getString("category_name"));
                }

                return new Vocabulary(vocabularyId, vocabulary, kana, senses, categories);
            }
        }
        catch(SQLException ex) {
            Logger.getLogger(DataDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
