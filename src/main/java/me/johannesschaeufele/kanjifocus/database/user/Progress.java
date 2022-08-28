package me.johannesschaeufele.kanjifocus.database.user;

import java.awt.Color;

/**
 * An enumeration of the different progress levels users can assign to learnable items, such as kanji, readings, or vocabulary
 * The different levels are supposed to be obtained via spaced repetition
 */
public enum Progress {

    UNKNOWN(0, "Unknown", new Color(200, 25, 25)),
    NEW(1, "New", new Color(235, 235, 15)),
    LEARNING(2, "Learning", new Color(190, 220, 50)),
    RECENT(3, "Recent", new Color(80, 220, 80)),
    REVISE(4, "Revise", new Color(40, 210, 40)),
    CHECK(5, "Check", new Color(20, 180, 20)),
    KNOWN(6, "Known", new Color(10, 150, 10));

    private final int id;
    private final String display;
    private final Color color;

    private Progress(int id, String display, Color color) {
        this.id = id;
        this.display = display;
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public String getDisplay() {
        return this.display;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.display;
    }

    public static final Progress[] byId;

    static {
        int maxId = 0;

        for(Progress progress : Progress.values()) {
            maxId = Math.max(progress.getId(), maxId);
        }

        byId = new Progress[maxId + 1];

        for(Progress progress : Progress.values()) {
            byId[progress.getId()] = progress;
        }

    }

}
