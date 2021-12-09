package thoenluk.aoc2021.challenge8;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class WireDespaghettifier implements ChristmasSaver {

    private static Map<String, Integer> SORTED_SEGMENTS_TO_DIGIT_CHAR = Map.ofEntries(
            entry("abcefg", 0),
            entry("cf", 1),
            entry("acdeg", 2),
            entry("acdfg", 3),
            entry("bcdf", 4),
            entry("abdfg", 5),
            entry("abdefg", 6),
            entry("acf", 7),
            entry("abcdefg", 8),
            entry("abcdfg", 9)
    );

    @Override
    public String saveChristmas(String input) {
        final String[] lines = Ut.splitMultilineString(input);

        int trivialDigitsInOutput = 0;

        for (String line : lines) {
            final String[] signalPatternsAndOutput = line.split(" \\| ");
            trivialDigitsInOutput += Arrays.stream(
                    signalPatternsAndOutput[1].split(Ut.WHITE_SPACE_REGEX))
                    .filter((digit) -> digit.length() <= 4 || digit.length() == 7)
                    .count();
        }
        return Integer.toString(trivialDigitsInOutput);
    }

    @Override
    public String saveChristmasAgain(String input) {
        final String[] lines = Ut.splitMultilineString(input);
        int sum = 0;

        for (String line : lines) {
            final String[] signalPatternsAndOutput = line.split(" \\| ");

            final Map<Character, Character> segmentMappings = getSegmentMappings(signalPatternsAndOutput[0]);
            sum += parseOutputValueWithMappings(signalPatternsAndOutput[1], segmentMappings);
        }
        return Integer.toString(sum);
    }

    private Map<Character, Character> getSegmentMappings(String entry) {
        List<String> signalPatterns = new ArrayList<>(Arrays.asList(entry.split(Ut.WHITE_SPACE_REGEX)));
        signalPatterns.sort(Comparator.comparing(String::length));

        final String one = signalPatterns.get(0);
        String two = signalPatterns.get(3); // Random guess guaranteed to be correct (-ed below)
        String three = signalPatterns.get(5);
        final String four = signalPatterns.get(2);
        String five = signalPatterns.get(4);
        final String seven = signalPatterns.get(1);

        // 1. Determine segment A which is the difference between 7 and 1.
        final String a = distinct(seven, one);

        // 2. Find if three is actually three.
        if (distinct(three, one).length() != 3) {
            if (distinct(two, one).length() == 3) {
                final String actuallyTwo = three;
                three = two;
                two = actuallyTwo;
            }
            else {
                final String actuallyFive = three;
                three = five;
                five = actuallyFive;
            }
        }

        // 3. Find if five is actually five.
        if (distinct(five, four).length() != 2) {
            two = five;
        }

        // 4. Determine segment G which is three minus four and a
        final String g = distinct(three, four, a);

        // 5. Determine segment D = three - one, a, g
        final String d = distinct(three, one, a, g);

        // 6. Determine segment B which is four minus one and d
        final String b = distinct(four, one, d);

        // 7. Determine segment E = two - four, a, g
        final String e = distinct(two, four, a, g);

        // 8. Determine segment C = two - a, d, e, g
        final String c = distinct(two, a, d, e, g);

        // 9. Determine segment F = one - c
        final String f = distinct(one, c);

        // 10. Enjoy the musical ladder or whatever it is we made.
        return Map.ofEntries(
                entry(a.charAt(0), 'a'),
                entry(b.charAt(0), 'b'),
                entry(c.charAt(0), 'c'),
                entry(d.charAt(0), 'd'),
                entry(e.charAt(0), 'e'),
                entry(f.charAt(0), 'f'),
                entry(g.charAt(0), 'g'));
    }

    private String distinct(String original, String... stringsToRemove) {
        for (String toRemove : stringsToRemove) {
            original = original.replaceAll("[" + toRemove + "]", "");
        }
        return original;
    }

    private int parseOutputValueWithMappings(String outputValue, Map<Character, Character> mappings) {
        final String[] digits = outputValue.split(Ut.WHITE_SPACE_REGEX);
        int parsedValue = 0;

        for (String digit : digits) {
            parsedValue *= 10;
            final char[] segments = digit.toCharArray();
            for (int i = 0; i < segments.length; i++) {
                segments[i] = mappings.get(segments[i]);
            }
            Arrays.sort(segments);
            parsedValue += SORTED_SEGMENTS_TO_DIGIT_CHAR.get(String.valueOf(segments));
        }

        return parsedValue;
    }
}
