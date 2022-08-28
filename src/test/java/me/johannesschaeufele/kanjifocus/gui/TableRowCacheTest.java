package me.johannesschaeufele.kanjifocus.gui;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TableRowCacheTest {

    @Test
    public void testTableRowCache() {
        TableRowCache<Integer> cache = new TableRowCache<>(Integer.class);
        cache.initialize(10);

        for(int i = 0; i < 15; i++) {
            assertThat(cache.get(i), nullValue());
        }

        cache.set(0, 0);
        assertThat(cache.get(0), is(0));
        assertThat(cache.get(1), nullValue());
        assertThat(cache.get(0), is(0));

        cache.set(100, 100);
        assertThat(cache.get(100), is(100));
        assertThat(cache.get(0), nullValue());

        cache.set(0, 0);
        cache.set(1, 1);
        assertThat(cache.get(0), is(0));
        assertThat(cache.get(1), is(1));

        cache.clear();

        for(int i = 0; i < 15; i++) {
            assertThat(cache.get(i), nullValue());
        }
    }

}
