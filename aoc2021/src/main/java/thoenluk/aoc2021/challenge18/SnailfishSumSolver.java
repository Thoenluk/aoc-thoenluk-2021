package thoenluk.aoc2021.challenge18;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge18.numbers.Pair;
import thoenluk.aoc2021.ut.Ut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SnailfishSumSolver implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        List<Pair> pairs = parseInput(input);

        Pair head = pairs.remove(0);

        while (!pairs.isEmpty()) {
            head = head.add(pairs.remove(0)).reduce();
        }

        return Long.toString(head.getMagnitude());
    }

    @Override
    public String saveChristmasAgain(String input) {
        List<Pair> pairs = parseInput(input);

        long largestMagnitude = Integer.MIN_VALUE;
        long magnitude;
        Pair first, second;

        for (int i = 0; i < pairs.size(); i++) {
            for (int k = i + 1; k < pairs.size(); k++) {
                first = (Pair) pairs.get(i).copy();
                second = (Pair) pairs.get(k).copy();

                magnitude = first.add(second).reduce().getMagnitude();

                if (largestMagnitude < magnitude) {
                    largestMagnitude = magnitude;
                }

                first = (Pair) pairs.get(i).copy();
                second = (Pair) pairs.get(k).copy();

                magnitude = second.add(first).reduce().getMagnitude();

                if (largestMagnitude < magnitude) {
                    largestMagnitude = magnitude;
                }
            }
        }

        return Long.toString(largestMagnitude);
    }

    private List<Pair> parseInput(String input) {
        LinkedList<Character> encodedData = new LinkedList<>();
        String[] lines = Ut.splitMultilineString(input);

        List<Pair> pairs = new ArrayList<>();

        for (String line : lines) {
            Collections.addAll(encodedData,
                    line.chars()
                            .mapToObj(c -> (char) c)
                            .toArray(Character[]::new));
            encodedData.remove(); // Always [
            pairs.add(new Pair(encodedData, null));
        }

        return pairs;
    }
}
