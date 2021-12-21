package thoenluk.aoc2021.challenge3;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DiagnosticParser implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        final int individualLineLength = lines[0].length();
        final int[] numberOfOnesPerPosition = new int[individualLineLength];
        final int leastCountForMajority = (lines.length + 1) / 2;
        int position;

        for (String line : lines) {
            for (position = 0; position < individualLineLength; position++) {
                if (line.charAt(position) == '1') {
                    numberOfOnesPerPosition[position]++;
                }
            }
        }

        StringBuilder gammaRateBits = new StringBuilder();
        StringBuilder epsilonRateBits = new StringBuilder();

        for (position = 0; position < individualLineLength; position++) {
            if (leastCountForMajority <= numberOfOnesPerPosition[position]) {
                gammaRateBits.append("1");
                epsilonRateBits.append("0");
            }
            else {
                gammaRateBits.append("0");
                epsilonRateBits.append("1");
            }
        }

        final int gammaRate = Integer.parseInt(gammaRateBits.toString(), 2);
        final int epsilonRate = Integer.parseInt(epsilonRateBits.toString(), 2);

        return Integer.toString(gammaRate * epsilonRate);
    }

    @Override
    public String saveChristmasAgain(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);

        List<String> potentialOxygenGeneratorRatings = new LinkedList<>(Arrays.asList(lines));
        List<String> potentialCO2ScrubberRatings = new LinkedList<>(Arrays.asList(lines));

        final int oxygenGeneratorRating = determineRating(potentialOxygenGeneratorRatings, false);
        final int co2ScrubberRating = determineRating(potentialCO2ScrubberRatings, true);

        return Integer.toString(oxygenGeneratorRating * co2ScrubberRating);
    }

    private int determineRating(List<String> potentialRatings, boolean invert) {
        int numberOfOnes;
        int leastCountForMajority;
        final int individualLineLength = potentialRatings.get(0).length();
        StringBuilder prefix = new StringBuilder();

        for (int position = 0; position < individualLineLength; position++) {
            numberOfOnes = 0;
            leastCountForMajority = (potentialRatings.size() + 1) / 2;

            for (String rating : potentialRatings) {
                if (rating.charAt(position) == '1') {
                    numberOfOnes++;
                }
            }

            if (leastCountForMajority <= numberOfOnes) {
                prefix.append(invert ? "0" : "1");
            }
            else {
                prefix.append(invert ? "1" : "0");
            }

            potentialRatings.removeIf((rating) -> !rating.startsWith(prefix.toString()));

            if (potentialRatings.size() == 1) {
                return Integer.parseInt(potentialRatings.get(0), 2);
            }
        }
        throw new IllegalArgumentException("I goofed it- I mean no valid rating exists, which I'm pretty sure is mathematically" +
                "impossible and this line will never be reached but this function wouldn't compile if it didn't have a guaranteed" +
                "return / throw instruction.");
    }
}
