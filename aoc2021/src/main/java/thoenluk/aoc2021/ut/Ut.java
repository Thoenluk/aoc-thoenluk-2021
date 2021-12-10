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

    private static final Map<String, Integer> STRING_INTEGER_CACHE = new HashMap<>();
    private static final Map<Character, Integer> CHAR_INTEGER_CACHE = new HashMap<>();


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

    public static int overflowSafeSum(int... summands) {
        int result = 0;
        for (int summand : summands) {
            if (Integer.MAX_VALUE - result < summand) {
                throw new AssertionError("Sum would overflow! Use a long instead.");
            }
            result += summand;
        }
        return result;
    }

    public static int overflowSafeProduct(int... multiplicands) {
        int result = 1;
        for (int multiplicand : multiplicands) {
            if (Integer.MAX_VALUE / result <= multiplicand) {
                throw new AssertionError("Product would overflow! Use a long instead.");
            }
            result *= multiplicand;
        }
        return result;
    }

    public static int cachedParseInt(String stringRepresentation) {
        return STRING_INTEGER_CACHE.computeIfAbsent(stringRepresentation, Integer::parseInt);
    }

    public static int cachedGetNumericValue(char charRepresentation) {
        return CHAR_INTEGER_CACHE.computeIfAbsent(charRepresentation, Character::getNumericValue);
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
        return csv.replaceAll(NEWLINE_REGEX, "").split(",");
    }
}
