package me.johannesschaeufele.kanjifocus.gui;

import me.johannesschaeufele.kanjifocus.database.user.Progress;

/**
 * A class representing the visible contents of learnable cells, containing a string typically rendered in the cell
 * and a progress value which determines the background color of the cell
 */
public class ValuePair {

    private final String value;
    private final Progress progress;

    public ValuePair(String value, Progress progress) {
        this.value = value;
        this.progress = progress;
    }

    public Progress getProgress() {
        return this.progress;
    }

    public String getValue() {
        return this.value;
    }

}
