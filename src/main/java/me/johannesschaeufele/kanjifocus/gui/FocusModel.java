package me.johannesschaeufele.kanjifocus.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import me.johannesschaeufele.kanjifocus.configuration.Configuration;
import me.johannesschaeufele.kanjifocus.database.data.DataDatabase;
import me.johannesschaeufele.kanjifocus.database.user.UserDatabase;
import me.johannesschaeufele.kanjifocus.util.JsonIO;
import me.johannesschaeufele.kanjifocus.util.Resources;

/**
 * Main model class for the application
 *
 * This follows the MVC (model view controller) approach
 */
public class FocusModel {

    private final DataDatabase dataDatabase;
    private final UserDatabase userDatabase;

    private File configurationFile;
    private Configuration configuration;

    private String kanji;

    public FocusModel() {
        this(null);
    }

    public FocusModel(Configuration configuration) {
        this.dataDatabase = new DataDatabase(Resources.getFile("res/data.db"));
        this.dataDatabase.initialize();

        this.userDatabase = new UserDatabase(Resources.getFile("user.db"));
        this.userDatabase.initialize();

        if(!this.userDatabase.isFilled()) {
            this.userDatabase.fill(this.dataDatabase.getKanjiList(null, null, null), this.dataDatabase.getVocabularyList(null, null, null, null));
        }

        if(configuration != null) {
            this.configuration = configuration;
            return;
        }

        boolean defaultConfig = true;

        File dataFolder = null;
        String env = System.getenv("APPDATA");
        if(env != null) {
            dataFolder = new File(env);
        }
        if(dataFolder == null || !dataFolder.exists()) {
            dataFolder = new File(System.getProperty("user.home"));
        }
        this.configurationFile = new File(new File(dataFolder, "KanjiFocus"), "config.json");
        if(this.configurationFile.exists()) {
            try {
                this.configuration = JsonIO.loadJSON(configurationFile, Configuration.class);
                defaultConfig = false;
            }
            catch(IOException ex) {
                Logger.getLogger(FocusModel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if(defaultConfig) {
            this.configuration = new Configuration();

            Object[] dialogOptions = {"Agree", "Exit"};
            int chosenOption = JOptionPane.showOptionDialog(
                    null,
                    "KanjiFocus\n\nThis software uses third-party projects that are subject to the conditions found in COPYING.txt.\nBy selecting 'Agree' and continuing to use this software,\n you confirm that you have read these conditions and agree to them.",
                    "KanjiFocus conditions",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    dialogOptions,
                    null
            );

            if(chosenOption != 0) {
                System.exit(0);
            }
        }
    }

    public void saveConfiguration() {
        if(this.configurationFile == null) {
            return;
        }

        if(!this.configurationFile.exists()) {
            this.configurationFile.getParentFile().mkdirs();
        }

        try {
            JsonIO.saveJSON(this.configurationFile, this.configuration);
        }
        catch(IOException ex) {
            Logger.getLogger(FocusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public DataDatabase getDataDatabase() {
        return this.dataDatabase;
    }

    public String getKanji() {
        return this.kanji;
    }

    public UserDatabase getUserDatabase() {
        return this.userDatabase;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

}
