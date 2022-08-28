package me.johannesschaeufele.kanjifocus.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RomajiUtilTest {

    @Test
    public void testKanaToRomajiHiragana() {
        assertThat(RomajiUtil.kanaToRomaji("いろはにほへと"), is("irohanihoheto"));
        assertThat(RomajiUtil.kanaToRomaji("やっと"), is("yatto"));
    }

    @Test
    public void testKanaToRomajiKatakana() {
        assertThat(RomajiUtil.kanaToRomaji("イロハニホヘト"), is("irohanihoheto"));
        assertThat(RomajiUtil.kanaToRomaji("ベッド"), is("beddo"));
    }

    @Test
    public void testKanaToRomajiMixedKana() {
        assertThat(RomajiUtil.kanaToRomaji("いろはにホヘト"), is("irohanihoheto"));
        assertThat(RomajiUtil.kanaToRomaji("へヘ"), is("hehe"));
    }

    @Test
    public void testKanaToRomajiChouon() {
        assertThat(RomajiUtil.kanaToRomaji("ハーフ"), is("haafu"));
        assertThat(RomajiUtil.kanaToRomaji("ヒー"), is("hii"));
        assertThat(RomajiUtil.kanaToRomaji("じー"), is("jii"));
        assertThat(RomajiUtil.kanaToRomaji("フー"), is("fuu"));
    }

    @Test
    public void testKanaToRomajiDiphthong() {
        assertThat(RomajiUtil.kanaToRomaji("きゃく"), is("kyaku"));
        assertThat(RomajiUtil.kanaToRomaji("きょう"), is("kyou"));
    }

    @Test
    public void testKanaToRomajiDakuten() {
        assertThat(RomajiUtil.kanaToRomaji("がざだばぱ"), is("gazadabapa"));
        assertThat(RomajiUtil.kanaToRomaji("ガザダバパ"), is("gazadabapa"));
        assertThat(RomajiUtil.kanaToRomaji("じ"), is("ji"));
        assertThat(RomajiUtil.kanaToRomaji("づ"), is("dzu"));
    }

    @Test
    public void testKanaToRomajiTi() {
        assertThat(RomajiUtil.kanaToRomaji("パーティー"), is("paatii"));
        assertThat(RomajiUtil.kanaToRomaji("ガンディー"), is("gandii"));
    }

    @Test
    public void testKanaToRomajiMixed() {
        assertThat(RomajiUtil.kanaToRomaji("ちょっと"), is("chotto"));
        assertThat(RomajiUtil.kanaToRomaji("ギョーザ"), is("gyouza"));
        assertThat(RomajiUtil.kanaToRomaji("ジャージ"), is("jaaji"));
        assertThat(RomajiUtil.kanaToRomaji("いろはにromajiホヘト"), is("irohaniromajihoheto"));

        RomajiUtil.kanaToRomaji("っ");
        RomajiUtil.kanaToRomaji("ー");
    }

}
