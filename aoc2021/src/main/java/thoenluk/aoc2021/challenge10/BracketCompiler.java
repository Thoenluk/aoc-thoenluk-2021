package thoenluk.aoc2021.challenge10;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BracketCompiler implements ChristmasSaver {
    // If you think this is impressive, you should see the other compiler I've written at https://github.com/Thoenluk/elf-code-computer.

    @Override
    public String saveChristmas(String input) {
        String[] lines = UtStrings.splitMultilineString(input);

        int score = 0;

        for (String line : lines) {
            score += getSyntaxErrorScoreOfLine(line);
        }

        return Integer.toString(score);
    }

    private int getSyntaxErrorScoreOfLine(String line) {
        final Stack<Character> chunkOpeningCharacters = new Stack<>();

        for (char character : line.toCharArray()) {
            switch (character) {
                case '(', '[', '{', '<' -> chunkOpeningCharacters.push(character);
                case ')' -> {
                    if (chunkOpeningCharacters.pop() != '(') return 3;
                }
                case ']' -> {
                    if (chunkOpeningCharacters.pop() != '[') return 57;
                }
                case '}' -> {
                    if (chunkOpeningCharacters.pop() != '{') return 1197;
                }
                case '>' -> {
                    if (chunkOpeningCharacters.pop() != '<') return 25137;
                }
            }
        }

        return 0;
    }

    @Override
    public String saveChristmasAgain(String input) {
        String[] lines = UtStrings.splitMultilineString(input);
        List<Long> autocompleteScores = new ArrayList<>();
        long score;

        for (String line : lines) {
            score = getAutocompleteScoreOfLine(line);
            if (score != 0) {
                autocompleteScores.add(score);
            }
        }

        autocompleteScores.sort(null);

        return Long.toString(autocompleteScores.get(autocompleteScores.size() / 2));
    }

    private long getAutocompleteScoreOfLine(String line) {
        final Stack<Character> expectedClosingCharacters = new Stack<>();

        for (char character : line.toCharArray()) {
            switch (character) {
                case '(' -> expectedClosingCharacters.push(')');
                case '[' -> expectedClosingCharacters.push(']');
                case '{' -> expectedClosingCharacters.push('}');
                case '<' -> expectedClosingCharacters.push('>');
                case ')', ']', '}', '>' -> {
                    if (expectedClosingCharacters.pop() != character) return 0;
                }
            }
        }

        long score = 0;
        char missingCloser;

        while (!expectedClosingCharacters.isEmpty()) {
            missingCloser = expectedClosingCharacters.pop();
            score *= 5;

            switch (missingCloser) {
                case ')' -> score += 1;
                case ']' -> score += 2;
                case '}' -> score += 3;
                case '>' -> score += 4;
            }
        }

        return score;
    }
}
