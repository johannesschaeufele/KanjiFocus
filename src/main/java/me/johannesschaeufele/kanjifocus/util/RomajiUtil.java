package me.johannesschaeufele.kanjifocus.util;

/**
 * A utlity class that allows strings with kana to be romanized
 * This can be used to romanize readings, when kana representations of the readings are available
 */
public final class RomajiUtil {

    private static final char hiraganaStart = '\u3040';
    private static final char hiraganaEnd = hiraganaStart + 87;

    private static final char katakanaStart = '\u30A0';
    private static final char katakanaEnd = katakanaStart + 91;

    private static final int smallTsuOffset = 'っ' - hiraganaStart;

    private static final char katakanaChouon = 'ー';

    private static final String[] kanaRomaji = new String[]{
            null,
            "xa", "a", "xi", "i", "xu", "u", "xe", "e", "xo", "o",
            "ka", "ga", "ki", "gi", "ku", "gu", "ke", "ge", "ko", "go",
            "sa", "za", "shi", "ji", "su", "zu", "se", "ze", "so", "zo",
            "ta", "da", "chi", "dji", "xtsu", "tsu", "dzu", "te", "de", "to", "do",
            "na", "ni", "nu", "ne", "no",
            "ha", "ba", "pa", "hi", "bi", "pi", "fu", "bu", "pu", "he", "be", "pe", "ho", "bo", "po",
            "ma", "mi", "mu", "me", "mo",
            "xya", "ya", "xyu", "yu", "xyo", "yo",
            "ra", "ri", "ru", "re", "ro",
            "xwa", "wa", "wi", "we", "wo",
            "n",
            "vu",
            "xka", "xke",
            "va", "vi", "ve", "vo",
            " "
    };

    /**
     * Romanizes a string by replacing all occurrences of full-width hiragana and katakana by their romanizations
     * Characters that are not full-width kana are retained in unchanged form
     *
     * The romanization closely follows how the corresponding kana would typically be typed on a keyboard using IME
     * Long vowels are written as several characters rather than a combined character with a macron
     *
     * Some ambiguous cases may be not be resolved canonically, such as "えー" being romanized as "ei" instead of "ee"
     *
     * @param kana The string to be romanized
     * @return The romanized string
     */
    public static String kanaToRomaji(String kana) {
        StringBuilder sb = new StringBuilder();

        String lastVowel = "-";
        boolean doubleNext = false;

        char[] chars = kana.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            boolean doubleCurrent = doubleNext;
            doubleNext = false;

            char current = chars[i];

            char kanaStart;
            if(current > hiraganaStart && current <= hiraganaEnd) {
                kanaStart = hiraganaStart;
            }
            else if((current > katakanaStart && current <= katakanaEnd) || (current == katakanaChouon)) {
                kanaStart = katakanaStart;
            }
            else {
                sb.append(current);
                continue;
            }

            int offset = current - kanaStart;

            String usedRomaji;

            if(offset == smallTsuOffset && i + 1 < chars.length) {
                usedRomaji = "tsu";
                doubleNext = true;
            }
            else if(current == katakanaChouon) {
                // Note: ee and oo are more appropriate in some cases
                switch(lastVowel) {
                    default:
                        usedRomaji = lastVowel;
                        break;

                    case "e":
                        usedRomaji = "i";
                        break;

                    case "o":
                        usedRomaji = "u";
                        break;
                }

            }
            else {
                String romajiBase = kanaRomaji[offset];

                usedRomaji = romajiBase;

                if(i + 1 < chars.length) {
                    char next = chars[i + 1];
                    char nextStart = 0;

                    if(next > hiraganaStart && next <= hiraganaEnd) {
                        nextStart = hiraganaStart;
                    }
                    else if(next > katakanaStart && next <= katakanaEnd) {
                        nextStart = katakanaStart;
                    }

                    if(nextStart != 0) {
                        String nextRomajiBase = kanaRomaji[next - nextStart];

                        boolean processed = false;
                        if(romajiBase.endsWith("i")) {
                            if(nextRomajiBase.startsWith("xy")) {
                                if(romajiBase.equals("chi") || romajiBase.equals("shi") || romajiBase.equals("ji")) {
                                    usedRomaji = romajiBase.substring(0, romajiBase.length() - 1) + nextRomajiBase.substring(2);
                                }
                                else {
                                    usedRomaji = romajiBase.substring(0, romajiBase.length() - 1) + nextRomajiBase.substring(1);
                                }

                                processed = true;

                                i++;
                            }
                        }

                        if(!processed) {
                            if((romajiBase.equals("te") || romajiBase.equals("de") || romajiBase.equals("ji")) && nextRomajiBase.startsWith("x")) {
                                usedRomaji = romajiBase.charAt(0) + nextRomajiBase.substring(1);
                                i++;
                            }
                        }
                    }
                }
            }

            if(!doubleNext) {
                if(doubleCurrent) {
                    sb.append(usedRomaji.charAt(0));
                }
                sb.append(usedRomaji);
            }

            lastVowel = usedRomaji.charAt(usedRomaji.length() - 1) + "";
        }

        return sb.toString();
    }

    private RomajiUtil() {}

}
