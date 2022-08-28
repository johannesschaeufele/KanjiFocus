package me.johannesschaeufele.kanjifocus.util;

import com.atilika.kuromoji.unidic.Token;
import com.atilika.kuromoji.unidic.Tokenizer;
import java.util.List;

/**
 *
 */
public final class ReadingUtil {

    private static final Tokenizer tokenizer = new Tokenizer.Builder().build();

    public static boolean usesReading(String voc, String vocKana, String kanji, String reading) {
        boolean allowFirst = reading.charAt(0) != '-';
        boolean allowLast = reading.charAt(reading.length() - 1) != '-';
        int splitIndex = reading.indexOf('.');
        boolean split = splitIndex >= 0;

        if(!allowFirst) {
            reading = reading.substring(1);
        }
        if(!allowLast) {
            reading = reading.substring(0, reading.length() - 1);
        }

        String r = null;
        String rS = null;

        if(split) {
            r = RomajiUtil.kanaToRomaji(reading.substring(0, splitIndex));
            reading = reading.replace(".", "");

            rS = RomajiUtil.kanaToRomaji(reading.substring(splitIndex));
        }

        List<Token> tokens = tokenizer.tokenize(voc);
        String s = "";
        for(Token token : tokens) {
            s += token.getPronunciation();
        }

        String rr = RomajiUtil.kanaToRomaji(reading);
        String kr = RomajiUtil.kanaToRomaji(vocKana);
        if(!RomajiUtil.kanaToRomaji(s).equals(kr)) {
            if(!allowFirst) {
                kr = kr.substring(1);
            }
            if(!allowLast) {
                kr = kr.substring(0, kr.length() - 1);
            }

            return kr.contains(rr);
        }

        boolean nextP = false;
        for(int tokenIndex = 0; tokenIndex < tokens.size(); tokenIndex++) {
            Token token = tokens.get(tokenIndex);

            String surface = token.getSurface();
            String pron = token.getPronunciation();
            if(tokenIndex == 0 && !allowFirst) {
                surface = surface.substring(1);
                pron = pron.substring(1);
            }
            else if(tokenIndex == tokens.size() - 1 && !allowLast) {
                surface = surface.substring(0, surface.length() - 1);
                pron = pron.substring(0, pron.length() - 1);
            }

            if(nextP) {
                if(RomajiUtil.kanaToRomaji(pron).startsWith(rS)) {
                    return true;
                }

                nextP = false;
            }

            if(token.getSurface().contains(kanji)) {
                String pr = RomajiUtil.kanaToRomaji(pron);
                if(pr.contains(rr)) {
                    return true;
                }
                else if(split && pr.endsWith(r)) {
                    nextP = true;
                }
            }
        }

        return false;
    }

    private ReadingUtil() {}

}
