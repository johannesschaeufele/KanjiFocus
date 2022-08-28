package me.johannesschaeufele.kanjifocus.database.user;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.johannesschaeufele.kanjifocus.database.Database;
import me.johannesschaeufele.kanjifocus.database.data.DataDatabase;

/**
 * This class allows writing to and reading from the user database containing the user's current progress
 * with kanji, and its readinings and meanings, as well as vocabulary and its senses
 */
public class UserDatabase extends Database {

    private boolean filled = false;

    public UserDatabase(File file) {
        super(file);
    }

    @Override
    public void initialize() {
        try {
            this.connect(false);

            if(this.connection.getMetaData().getTables(null, null, "kanji_progress", null).next()) {
                this.filled = true;
            }
            else {
                this.connection.createStatement().execute(
                        "CREATE TABLE kanji_progress("
                                + "id          INTEGER         NOT NULL,"
                                + "progress    INTEGER(3)      NOT NULL,"
                                + "meaning     INTEGER(3)      NOT NULL,"
                                + "read        INTEGER(3)      NOT NULL,"
                                + "write       INTEGER(3)      NOT NULL,"
                                + ""
                                + "CONSTRAINT kanji_progress_pk"
                                + "  PRIMARY KEY(id)"
                                + ");"
                );
                this.connection.createStatement().execute(
                        "CREATE TABLE readings_progress("
                                + "id          INTEGER         NOT NULL,"
                                + "progress    INTEGER(3)      NOT NULL,"
                                + ""
                                + "CONSTRAINT readings_progress_pk"
                                + "  PRIMARY KEY(id)"
                                + ");"
                );
                this.connection.createStatement().execute(
                        "CREATE TABLE nanori_progress("
                                + "id          INTEGER         NOT NULL,"
                                + "progress    INTEGER(3)      NOT NULL,"
                                + ""
                                + "CONSTRAINT nanori_progress_pk"
                                + "  PRIMARY KEY(id)"
                                + ");"
                );
                this.connection.createStatement().execute(
                        "CREATE TABLE vocabulary_progress("
                                + "id          INTEGER         NOT NULL,"
                                + "progress    INTEGER(3)      NOT NULL,"
                                + ""
                                + "CONSTRAINT vocabulary_progress_pk"
                                + "  PRIMARY KEY(id)"
                                + ");"
                );
                this.connection.createStatement().execute(
                        "CREATE TABLE senses_progress("
                                + "id          INTEGER         NOT NULL,"
                                + "progress    INTEGER(3)      NOT NULL,"
                                + ""
                                + "CONSTRAINT senses_progress_pk"
                                + "  PRIMARY KEY(id)"
                                + ");"
                );

                this.connection.commit();
            }

        }
        catch(SQLException ex) {
            Logger.getLogger(DataDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Integer> filterProgress(String tableName, List<Integer> ids, List<Progress> progress) {
        if(ids == null || ids.isEmpty()) {
            return ids;
        }

        if(progress == null || progress.size() == Progress.values().length) {
            return ids;
        }
        else if(progress.isEmpty()) {
            return Arrays.asList();
        }

        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT id "
                    + "FROM ").append(tableName).append(
                    " WHERE id IN (?");

            for(int i = 1; i < ids.size(); i++) {
                sb.append(", ?");
            }

            sb.append(") AND progress IN (?");

            for(int i = 1; i < progress.size(); i++) {
                sb.append(", ?");
            }
            sb.append(")");

            PreparedStatement statementFilter = this.connection.prepareStatement(sb.toString());

            int i = 0;
            for(; i < ids.size(); i++) {
                statementFilter.setInt(i + 1, ids.get(i));
            }
            for(int j = 0; j < progress.size(); j++) {
                statementFilter.setInt(i + j + 1, progress.get(j).getId());
            }

            ResultSet result = statementFilter.executeQuery();

            List<Integer> filtered = new ArrayList<>(ids.size() / 4);
            while(result.next()) {
                filtered.add(result.getInt("id"));
            }

            List<Integer> filteredIds = new ArrayList<>(ids);
            Iterator<Integer> it = filteredIds.iterator();
            while(it.hasNext()) {
                if(!filtered.contains(it.next())) {
                    it.remove();
                }
            }

            return filteredIds;
        }
        catch(SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private Progress getProgress(String tableName, int id) {
        try {
            PreparedStatement statementProgress = this.connection.prepareStatement(
                    "SELECT progress "
                            + "FROM " + tableName + " "
                            + "WHERE id = ?"
            );

            statementProgress.setInt(1, id);

            ResultSet result = statementProgress.executeQuery();

            if(result.next()) {
                return Progress.byId[result.getInt("progress")];
            }
            else {
                return Progress.UNKNOWN;
            }
        }
        catch(SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void setProgress(String tableName, int id, Progress progress) {
        try {
            PreparedStatement statementProgress = this.connection.prepareStatement(
                    "REPLACE INTO " + tableName + "(id, progress) "
                            + "VALUES (?, ?)"
            );

            statementProgress.setInt(1, id);
            statementProgress.setInt(2, progress.getId());

            statementProgress.execute();

            this.connection.commit();
        }
        catch(SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Fills the database with unknown progress entries for the specified kanji and vocabulary if it is not already filled
     *
     * @param kanji List of kanji
     * @param vocabulary List of vocabulary
     */
    public void fill(List<String> kanji, List<Integer> vocabulary) {
        try {
            int unknownId = Progress.UNKNOWN.getId();

            PreparedStatement statementKanjiProgress = this.connection.prepareStatement(
                    "INSERT INTO kanji_progress(id, progress, read, write, meaning) "
                            + "VALUES (?, ?, ?, ?, ?)"
            );

            for(String k : kanji) {
                statementKanjiProgress.setInt(1, Character.codePointAt(k, 0));
                statementKanjiProgress.setInt(2, unknownId);
                statementKanjiProgress.setInt(3, unknownId);
                statementKanjiProgress.setInt(4, unknownId);
                statementKanjiProgress.setInt(5, unknownId);

                statementKanjiProgress.addBatch();
            }

            PreparedStatement statementVocabularyProgress = this.connection.prepareStatement(
                    "INSERT INTO vocabulary_progress(id, progress) "
                            + "VALUES (?, ?)"
            );
            for(int voc : vocabulary) {
                statementVocabularyProgress.setInt(1, voc);
                statementVocabularyProgress.setInt(2, unknownId);

                statementVocabularyProgress.addBatch();
            }

            statementKanjiProgress.executeBatch();
            statementVocabularyProgress.executeBatch();

            this.connection.commit();

            this.filled = true;
        }
        catch(SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a filtered list of the given kanji, only containing kanji that have one of the given progress values
     *
     * @param kanji List of kanji to filter
     * @param progress List of progress values to filter for
     * @return Filtered list of kanji
     */
    public List<String> filterKanjiProgress(List<String> kanji, List<Progress> progress) {
        List<Integer> ids = new ArrayList<>(kanji.size());
        for(String k : kanji) {
            ids.add(Character.codePointAt(k, 0));
        }

        List<Integer> filteredIds = this.filterProgress("kanji_progress", ids, progress);

        if(filteredIds == null) {
            return null;
        }

        List<String> filteredKanji = new ArrayList<>(filteredIds.size());

        for(int id : filteredIds) {
            filteredKanji.add(new String(new int[]{id}, 0, 1));
        }

        return filteredKanji;
    }

    /**
     * Returns a filtered list of the given vocabulary, only containing vocabulary that has one of the given progress values
     *
     * @param vocabulary List of vocabulary to filter
     * @param progress List of progress values to filter for
     * @return Filtered list of vocabulary
     */
    public List<Integer> filterVocabularyProgress(List<Integer> vocabulary, List<Progress> progress) {
        return this.filterProgress("vocabulary_progress", vocabulary, progress);
    }

    /**
     * Returns the progress values of a kanji, its meaning, and the ability to read and write it
     *
     * @param kanji Kanji to retrieve the progress of
     * @return Array of progress values for the kanji, its meaning, the ability to read it, and the ability to write it
     */
    public Progress[] getKanjiProgress(String kanji) {
        try {
            PreparedStatement statementReadingsProgress = this.connection.prepareStatement(
                    "SELECT progress, meaning, read, write "
                            + "FROM kanji_progress "
                            + "WHERE id = ?"
            );

            statementReadingsProgress.setInt(1, Character.codePointAt(kanji, 0));

            ResultSet result = statementReadingsProgress.executeQuery();

            if(result.next()) {
                return new Progress[]{
                        Progress.byId[result.getInt("progress")],
                        Progress.byId[result.getInt("meaning")],
                        Progress.byId[result.getInt("read")],
                        Progress.byId[result.getInt("write")]
                };
            }
        }
        catch(SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Returns the progress value for the nanori with the given id
     *
     * @param nanoriId nanori id
     * @return Progress value
     */
    public Progress getNanoriProgress(int nanoriId) {
        return this.getProgress("nanori_progress", nanoriId);
    }

    /**
     * Returns the progress value for the reading with the given id
     *
     * @param readingId reading id
     * @return Progress value
     */
    public Progress getReadingProgress(int readingId) {
        return this.getProgress("readings_progress", readingId);
    }

    /**
     * Returns the progress value for the sense with the given id
     *
     * @param senseId sense id
     * @return Progress value
     */
    public Progress getSenseProgress(int senseId) {
        return this.getProgress("senses_progress", senseId);
    }

    /**
     * Returns the progress value for the vocabulary with the given id
     *
     * @param vocabularyId vocabulary id
     * @return Progress value
     */
    public Progress getVocabularyProgress(int vocabularyId) {
        return this.getProgress("vocabulary_progress", vocabularyId);
    }

    /**
     * Returns whether the database has been filled with default values already
     *
     * @return Whether the database has been filled already
     */
    public boolean isFilled() {
        return this.filled;
    }

    /**
     * Writes the given progress value for the given kanji to the database
     *
     * @param kanji Kanji to write progress for
     * @param progress Progress values for the kanji, its meaning, the ability to read it, and the ability to write it
     */
    public void setKanjiProgress(String kanji, Progress... progress) {
        if(progress.length != 4) {
            throw new IllegalArgumentException();
        }

        try {
            PreparedStatement statementReadingsProgress = this.connection.prepareStatement(
                    "REPLACE INTO kanji_progress(id, progress, meaning, read, write) "
                            + "VALUES (?, ?, ?, ?, ?)"
            );

            statementReadingsProgress.setInt(1, Character.codePointAt(kanji, 0));
            statementReadingsProgress.setInt(2, progress[0].getId());
            statementReadingsProgress.setInt(3, progress[1].getId());
            statementReadingsProgress.setInt(4, progress[2].getId());
            statementReadingsProgress.setInt(5, progress[3].getId());

            statementReadingsProgress.execute();

            this.connection.commit();
        }
        catch(SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes the given progress value for the nanori with the given id to the database
     *
     * @param nanoriId Nanori id
     * @param progress Progress value
     */
    public void setNanoriProgress(int nanoriId, Progress progress) {
        this.setProgress("nanori_progress", nanoriId, progress);
    }

    /**
     * Writes the given progress value for the reading with the given id to the database
     *
     * @param readingId Reading id
     * @param progress Progress value
     */
    public void setReadingProgress(int readingId, Progress progress) {
        this.setProgress("readings_progress", readingId, progress);
    }

    /**
     * Writes the given progress value for the sense with the given id to the database
     *
     * @param senseId Sense id
     * @param progress Progress value
     */
    public void setSenseProgress(int senseId, Progress progress) {
        this.setProgress("senses_progress", senseId, progress);
    }

    /**
     * Writes the given progress value for the vocabulary with the given id to the database
     *
     * @param vocabularyId Vocabulary id
     * @param progress Progress value
     */
    public void setVocabularyProgress(int vocabularyId, Progress progress) {
        this.setProgress("vocabulary_progress", vocabularyId, progress);
    }

}
