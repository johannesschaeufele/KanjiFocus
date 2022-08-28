package me.johannesschaeufele.kanjifocus.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReadingUtilTest {

    @Test
    public void testReadingMatch() {
        assertTrue(ReadingUtil.usesReading("本日", "ほんじつ", "日", "ジツ"));
        assertFalse(ReadingUtil.usesReading("本日", "ほんじつ", "日", "ひ"));

        assertTrue(ReadingUtil.usesReading("本日", "ホンジツ", "日", "ジツ"));
        assertFalse(ReadingUtil.usesReading("本日", "ホンジツ", "日", "ひ"));
    }

    @Test
    public void testReadingMatchAllowFirst() {
        assertTrue(ReadingUtil.usesReading("三日", "みっか", "日", "-か"));
        assertFalse(ReadingUtil.usesReading("三日", "みっか", "日", "ニチ"));
    }

    @Test
    public void testReadingMatchAllowLast() {
        assertTrue(ReadingUtil.usesReading("御宅", "おたく", "御", "お-"));
        assertFalse(ReadingUtil.usesReading("御宅", "おたく", "御", "ゴ"));
    }

    @Test
    public void testReadingMatchSplit() {
        assertTrue(ReadingUtil.usesReading("一つ", "ひとつ", "一", "ひと.つ"));
        assertFalse(ReadingUtil.usesReading("一つ", "ひとつ", "一", "イチ"));
    }

    @Test
    public void testReadingCombined() {
        assertTrue(ReadingUtil.usesReading("音読み", "おんよみ", "読", "-よ.み"));
        assertFalse(ReadingUtil.usesReading("音読み", "おんよみ", "読", "よ.む"));
    }

}
