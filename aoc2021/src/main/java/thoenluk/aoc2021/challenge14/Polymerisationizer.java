package thoenluk.aoc2021.challenge14;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;

public class Polymerisationizer implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        return solveForIterationCount(input,10);
    }

    @Override
    public String saveChristmasAgain(String input) {
        return solveForIterationCount(input, 40);
    }

    private String solveForIterationCount(String input, int iterationCount) {
        final String[] lines = Ut.splitMultilineString(input);
        final int emptyLineIndex = Ut.getEmptyLinePositionInArray(lines);

        final String polymerTemplate = lines[0];
        final Map<String, String> pairInsertionRules = parsePairInsertionRules(lines, emptyLineIndex + 1);

        Map<String, Long> polymer = new HashMap<>();
        Map<String, Long> nextPolymer = new HashMap<>();

        for (int i = 0; i < polymerTemplate.length() - 1; i++) {
            polymer.compute(polymerTemplate.substring(i, i + 2), (key, value) -> value == null ? 1 : value + 1);
        }

        String pairKey, intermediateElement;

        for (int i = 0; i < iterationCount; i++) {
            for (Map.Entry<String, Long> pair : polymer.entrySet()) {
                pairKey = pair.getKey();
                final long finalPairValue = pair.getValue();
                intermediateElement = pairInsertionRules.get(pairKey);

                nextPolymer.compute(pairKey.charAt(0) + intermediateElement, (k, v) -> v == null ? finalPairValue : v + finalPairValue);
                nextPolymer.compute(intermediateElement + pairKey.charAt(1), (k, v) -> v == null ? finalPairValue : v + finalPairValue);
            }

            polymer = nextPolymer;
            nextPolymer = new HashMap<>();
        }

        final Map<Character, Long> occurences = new HashMap<>();

        for (Map.Entry<String, Long> pair : polymer.entrySet()) {
            pairKey = pair.getKey();
            final long finalPairValue = pair.getValue();

            occurences.compute(pairKey.charAt(0), (k, v) -> v == null ? finalPairValue : v + finalPairValue);
        }

        long mostOccurences = Long.MIN_VALUE;
        long leastOccurences = Long.MAX_VALUE;

        for (Long occurence : occurences.values()) {
            if (mostOccurences < occurence) {
                mostOccurences = occurence;
            }
            if (leastOccurences > occurence) {
                leastOccurences = occurence;
            }
        }

        return Long.toString(mostOccurences - leastOccurences + 1); // Exactly the most common letter is off by one,
        // but only with this approach while brute-force was fine. Praise to the one who explains to me why.
    }

    private Map<String, String> parsePairInsertionRules(String[] lines, int startIndex) {
        final Map<String, String> pairInsertionRules = new HashMap<>();
        String[] keyValue;

        for (int i = startIndex; i < lines.length; i++) {
            keyValue = lines[i].split(" -> ");
            pairInsertionRules.put(keyValue[0], keyValue[1]);
        }

        return pairInsertionRules;
    }
}
