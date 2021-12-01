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
    private static final Map<String, Integer> intCache = new HashMap<>();

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
        String[] lines = stringRepresentation.replaceAll("[\\s\\n\\r]+", "\t").split("\t");
        List<Integer> parsedList = new ArrayList<>(lines.length);
        for (String line : lines) {
            parsedList.add(cachedParseInt(line));
        }
        return parsedList;
    }
}
