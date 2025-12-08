package service;

public class GenreNormalizer {

    public static String normalize(String g) {
        if (g == null) return "unknown";

        g = g.toLowerCase();

        // ROCK FAMILY
        if (g.contains("rock") || g.contains("garage") || g.contains("brit")) return "rock";

        // POP FAMILY
        if (g.contains("pop")) return "pop";

        // HIPHOP FAMILY
        if (g.contains("hip") || g.contains("rap") || g.contains("trap")) return "hiphop";

        // ELECTRONIC FAMILY
        if (g.contains("edm") || g.contains("house") || g.contains("techno") || g.contains("electronic"))
            return "electronic";

        // ACOUSTIC
        if (g.contains("acoustic")) return "acoustic";

        // JAZZ
        if (g.contains("jazz")) return "jazz";

        // CLASSICAL
        if (g.contains("classic")) return "classical";

        // FALLBACK
        return g;
    }
}