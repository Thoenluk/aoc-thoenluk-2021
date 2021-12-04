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

    public static class Position {

        // Y coordinate (position on the vertical axis) comes first not only because that is common in computers,
        // but also because that is how you will end up iterating: First through the array of lines (vertical), then
        // through the chars of each line (horizontal.)
        private final int y, x;

        public Position(int y, int x) {
            this.y = y;
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }
    }
}
