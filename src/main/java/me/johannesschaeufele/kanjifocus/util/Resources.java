package me.johannesschaeufele.kanjifocus.util;

import java.io.File;

public final class Resources {

    public static File getFile(String path) {
        File source = new File(Resources.class.getProtectionDomain().getCodeSource().getLocation().getFile());

        // Handle running from a packaged archive
        if(source.isFile()) {
            return new File(source.getParentFile(), path);
        }
        else {
            return new File(path);
        }
    }

    private Resources() {
    }

}
