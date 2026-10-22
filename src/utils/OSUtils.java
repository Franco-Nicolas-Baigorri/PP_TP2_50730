package utils;

import java.util.Locale;

/** Utilidad conservada del proyecto base para consultar el sistema operativo. */
public class OSUtils {
    public static String getOs() {
        String nombre = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (nombre.startsWith("windows")) {
            return "WINDOWS";
        }
        return nombre.contains("mac") ? "MAC" : "LINUX";
    }
}
