package me.johannesschaeufele.kanjifocus.database.user;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class UserDatabaseTest {

    private File userDatabaseFile;
    private UserDatabase userDatabase;

    private void initializeUserDatabase() {
        this.userDatabase = new UserDatabase(this.userDatabaseFile);
        this.userDatabase.initialize();
    }

    @Test
    public void testUserDatabasePersistence() {
        Progress[] progress = new Progress[]{Progress.KNOWN, Progress.LEARNING, Progress.RECENT, Progress.REVISE};

        this.userDatabase.setKanjiProgress("一", progress);
        this.userDatabase.setReadingProgress(0, Progress.KNOWN);
        this.userDatabase.setNanoriProgress(0, Progress.REVISE);
        this.userDatabase.setSenseProgress(0, Progress.LEARNING);
        this.userDatabase.setVocabularyProgress(0, Progress.CHECK);

        assertThat(this.userDatabase.getKanjiProgress("一"), is(progress));
        assertThat(this.userDatabase.getReadingProgress(0), is(Progress.KNOWN));
        assertThat(this.userDatabase.getNanoriProgress(0), is(Progress.REVISE));
        assertThat(this.userDatabase.getSenseProgress(0), is(Progress.LEARNING));
        assertThat(this.userDatabase.getVocabularyProgress(0), is(Progress.CHECK));
    }

    @Test
    public void testUserDatabaseSerialization() {
        Progress[] progress = new Progress[]{Progress.KNOWN, Progress.LEARNING, Progress.RECENT, Progress.REVISE};

        this.userDatabase.setKanjiProgress("一", progress);
        this.userDatabase.setReadingProgress(0, Progress.KNOWN);
        this.userDatabase.setNanoriProgress(0, Progress.REVISE);
        this.userDatabase.setSenseProgress(0, Progress.LEARNING);
        this.userDatabase.setVocabularyProgress(0, Progress.CHECK);

        this.userDatabase.close();
        this.userDatabase = null;

        this.initializeUserDatabase();

        assertThat(this.userDatabase.getKanjiProgress("一"), is(progress));
        assertThat(this.userDatabase.getReadingProgress(0), is(Progress.KNOWN));
        assertThat(this.userDatabase.getNanoriProgress(0), is(Progress.REVISE));
        assertThat(this.userDatabase.getSenseProgress(0), is(Progress.LEARNING));
        assertThat(this.userDatabase.getVocabularyProgress(0), is(Progress.CHECK));
    }

    @Test
    public void testUserDatabaseFilterKanjiProgress() {
        Progress[] progressKnown = new Progress[]{Progress.KNOWN, Progress.KNOWN, Progress.KNOWN, Progress.KNOWN};
        Progress[] progressMixed = new Progress[]{Progress.LEARNING, Progress.NEW, Progress.RECENT, Progress.CHECK};

        this.userDatabase.setKanjiProgress("一", progressKnown);
        this.userDatabase.setKanjiProgress("二", progressMixed);
        this.userDatabase.setKanjiProgress("日", progressKnown);

        assertThat(this.userDatabase.filterKanjiProgress(Arrays.asList("一", "二", "日", "本"), Arrays.asList(Progress.KNOWN)), containsInAnyOrder("一", "日"));
        assertThat(this.userDatabase.filterKanjiProgress(Arrays.asList("一", "二", "日", "本"), Arrays.asList(Progress.REVISE)), containsInAnyOrder());
        assertThat(this.userDatabase.filterKanjiProgress(Arrays.asList("一", "二", "日", "本"), Arrays.asList(Progress.LEARNING)), containsInAnyOrder("二"));
        assertThat(this.userDatabase.filterKanjiProgress(Arrays.asList("一", "二", "日", "本"), Arrays.asList(Progress.NEW)), containsInAnyOrder());
    }

    @Test
    public void testUserDatabaseFilterVocabularyProgress() {
        this.userDatabase.setVocabularyProgress(0, Progress.KNOWN);
        this.userDatabase.setVocabularyProgress(1, Progress.LEARNING);
        this.userDatabase.setVocabularyProgress(2, Progress.REVISE);
        this.userDatabase.setVocabularyProgress(3, Progress.KNOWN);
        this.userDatabase.setVocabularyProgress(5, Progress.LEARNING);

        List<Integer> idList = Arrays.asList(0, 1, 2, 3, 4, 5, 6);

        assertThat(this.userDatabase.filterVocabularyProgress(idList, Arrays.asList(Progress.KNOWN)), containsInAnyOrder(0, 3));
        assertThat(this.userDatabase.filterVocabularyProgress(idList, Arrays.asList(Progress.LEARNING)), containsInAnyOrder(1, 5));
        assertThat(this.userDatabase.filterVocabularyProgress(idList, Arrays.asList(Progress.REVISE)), containsInAnyOrder(2));
        assertThat(this.userDatabase.filterVocabularyProgress(idList, Arrays.asList(Progress.NEW)), containsInAnyOrder());
    }

    @Before
    public void setUp() {
        this.userDatabaseFile = new File("test_user.db");
        this.userDatabaseFile.delete();

        this.initializeUserDatabase();
    }

    @After
    public void teardown() {
        this.userDatabase.close();
        this.userDatabaseFile.delete();
    }

}
