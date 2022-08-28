package me.johannesschaeufele.kanjifocus.database.data;

import java.util.Arrays;
import me.johannesschaeufele.kanjifocus.util.Resources;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class DataDatabaseTest {

    private DataDatabase dataDatabase;

    @Test
    public void testDataDatabasePopulated() {
        assertFalse(this.dataDatabase.getKanjiList(null, null, null).isEmpty());
        assertFalse(this.dataDatabase.getVocabularyReadings(null, null, null, null).isEmpty());
    }

    @Test
    public void testDataDatabaseKanjiReadings() {
        assertThat(this.dataDatabase.getKanjiList(null, null, Arrays.asList("ichi")), hasItem("一"));
        assertThat(this.dataDatabase.getKanjiList(null, null, Arrays.asList("jitsu")), hasItems("日", "実", "實"));
    }

    @Test
    public void testDataDatabaseKanjiTags() {
        assertThat(this.dataDatabase.getKanjiList(Arrays.asList("jlpt5"), null, null), hasItem("一"));
    }

    @Before
    public void setUp() {
        this.dataDatabase = new DataDatabase(Resources.getFile("res/data.db"));
        this.dataDatabase.initialize();
    }

}
