package me.johannesschaeufele.kanjifocus.database;

import java.io.Closeable;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class representing an SQL database, backed by SQLite databases stored in files
 */
public abstract class Database implements Closeable {

    private final File file;
    private boolean connected = false;

    protected Connection connection;

    public Database(File file) {
        this.file = file;
    }

    public abstract void initialize();

    protected final boolean isConnected() {
        return this.connected;
    }

    protected final void connect(boolean readOnly) throws SQLException {
        if(this.connected) {
            return;
        }

        Properties connectionProps = new Properties();
        connectionProps.setProperty("PRAGMA foreign_keys", "ON");

        if(readOnly) {
            connectionProps.setProperty("open_mode", "1");
        }

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath(), connectionProps);

        this.connection.setAutoCommit(false);

        this.connected = true;
    }

    protected final void release() throws SQLException {
        if(this.connected) {
            this.connection.close();
        }
    }

    @Override
    public void close() {
        try {
            this.release();
        }
        catch(SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
