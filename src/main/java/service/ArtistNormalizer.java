package service;

/**
 * Utility class for normalizing artist names.
 * Removes parenthetical content, special characters, and standardizes
 * formatting.
 */
public class ArtistNormalizer {

    public static String normalize(String name) {
        if (name == null)
            return "";

        name = name.toLowerCase();

        // remove parenthesis content: (Remaster), (Live), etc.
        name = name.replaceAll("\\(.*?\\)", "");

        // keep only letters, numbers, and spaces
        name = name.replaceAll("[^a-z0-9 ]", " ");

        // collapse whitespace
        name = name.replaceAll("\\s+", " ").trim();

        return name;
    }

    public static String extractPrimaryArtist(String raw) {
        if (raw == null)
            return "";
        String first = raw.split("[,/;]")[0]; // if multiple artists in string
        return normalize(first);
    }
}