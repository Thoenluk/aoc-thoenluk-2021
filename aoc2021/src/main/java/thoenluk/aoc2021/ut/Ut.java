package thoenluk.aoc2021.ut;

import java.util.*;

/**
 *
 * @author Lukas Thöni lukas.thoeni@gmx.ch
 */
public class Ut {

    //---- Statics

    public static final String WHITE_SPACE_REGEX = "[\\s\\n\\r]+";
    public static final String NEWLINE_REGEX = "\\r?\\n";

    private static final Map<String, Integer> intCache = new HashMap<>();


    //---- Methods

    public static void print(Object objToPrint) {
        System.out.print(objToPrint);
    }
    
    public static void println() {
        System.out.println();
    }
    
    public static void println(Object objToPrint) {
        System.out.println(objToPrint);
    }

    public static int cachedParseInt(String stringRepresentation) {
        if (!intCache.containsKey(stringRepresentation)) {
            intCache.put(stringRepresentation, Integer.parseInt(stringRepresentation));
        }
        return intCache.get(stringRepresentation);
    }

    public static List<Integer> multilineStringToIntegerList(String stringRepresentation) {
        String[] lines = splitMultilineString(stringRepresentation);
        List<Integer> parsedList = new ArrayList<>(lines.length);
        for (String line : lines) {
            parsedList.add(cachedParseInt(line));
        }
        return parsedList;
    }

    public static String[] splitMultilineString(String multiline) {
        return multiline.replaceAll(NEWLINE_REGEX, "\n").split("\n");
    }

    public static List<Integer> commaSeparatedStringToIntegerList(String csv) {
        String[] tokens = splitCommaSeparatedString(csv);
        List<Integer> parsedList = new ArrayList<>();
        for (String token : tokens) {
            parsedList.add(cachedParseInt(token));
        }
        return parsedList;
    }

    public static String[] splitCommaSeparatedString(String csv) {
        return csv.split(",");
    }

    public record Position(int y, int x) {}
}
