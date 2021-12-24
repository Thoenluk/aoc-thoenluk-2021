package thoenluk.aoc2021.challenge24.monad;

import thoenluk.aoc2021.ut.UtParsing;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MONAD {
    private final Stack<Integer> zValues = new Stack<>();
    final boolean[] iterationsPush;
    final int[] xAdders;
    final int[] yAdders;
    final Map<Integer, Integer> futureXAdders;

    public MONAD(String input) {
        String[] lines = UtStrings.splitMultilineString(input);
        iterationsPush = new boolean[lines.length / 18];
        xAdders = new int[lines.length / 18];
        yAdders = new int[lines.length / 18];
        futureXAdders = new HashMap<>();
        Stack<Integer> iterationsThatPush = new Stack<>();

        for (int i = 0; i * 18 < lines.length; i++) {
            iterationsPush[i] = getNumberFromLine(lines[i * 18 + 4]) == 1;
            xAdders[i] = getNumberFromLine(lines[i * 18 + 5]);
            yAdders[i] = getNumberFromLine(lines[i * 18 + 15]);

            if (iterationsPush[i]) {
                iterationsThatPush.push(i);
            }
            else {
                futureXAdders.put(iterationsThatPush.pop(), xAdders[i]);
            }
        }

        zValues.push(0);
    }

    private int getNumberFromLine(String line) {
        return UtParsing.cachedParseInt(line.replaceAll("[^-\\d]", ""));
    }

    public long getHighestValidModelNumber() {
        return getValidModelNumber(true);
    }

    public long getLowestValidModelNumber() {
        return getValidModelNumber(false);
    }

    private long getValidModelNumber(boolean findMax) {
        long modelNumber = 0;

        for (int i = 0; i < iterationsPush.length; i++) {
            modelNumber *= 10;
            modelNumber += getValidDigitForLiterals(i, findMax);
        }

        if (zValues.size() != 1 || zValues.pop() != 0) throw new AssertionError();
        return modelNumber;
    }

    private int getValidDigitForLiterals(int i, boolean findMax) {
        final int validDigit;

        int x = iterationsPush[i] ? zValues.peek() : zValues.pop();
        x += xAdders[i];

        if (x < 10) {
            validDigit = x;
        }
        else {
            final int totalYAdder = futureXAdders.get(i) + yAdders[i];
            validDigit = findMax ? Math.min(9 - totalYAdder, 9) : Math.max(1 - totalYAdder, 1);
            zValues.push(validDigit + yAdders[i]);
        }

        return validDigit;
    }
}
