package thoenluk.aoc2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lukas Thöni lukas.thoeni@gmx.ch
 */
public class Ut {

    //---- Statics

    public static final String WHITE_SPACE_REGEX = "[\\s\\n\\r]+";
    public static final String NEWLINE_REGEX = "[\\n\\r]+";

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

    public static List<Integer> stringToIntegerList(String stringRepresentation) {
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
}
