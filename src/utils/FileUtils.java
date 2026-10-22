package utils;

import java.io.File;

/** Nombres y ubicación de los archivos de eventos. */
public class FileUtils {
    public static final String PATH_EVENTS = "events" + File.separator;

    public static String getFormattedNameForEventID(String id) {
        return "Event" + id + ".pp";
    }
}
