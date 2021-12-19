package thoenluk.aoc2021.challenge13;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;
import java.util.function.UnaryOperator;

public class OrigamiOrganiser implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final String[] instructions = Ut.splitStringWithEmptyLines(input);
        final String[] dotInstructions = Ut.splitMultilineString(instructions[0]);
        final String[] foldInstructions = Ut.splitMultilineString(instructions[1]);

        Set<Position> dots = parseDotInstructions(dotInstructions);

        dots = executeFoldInstruction(foldInstructions[0], dots);

        return Integer.toString(dots.size());
    }

    @Override
    public String saveChristmasAgain(String input) {
        final String[] instructions = Ut.splitStringWithEmptyLines(input);
        final String[] dotInstructions = Ut.splitMultilineString(instructions[0]);
        final String[] foldInstructions = Ut.splitMultilineString(instructions[1]);

        Set<Position> dots = parseDotInstructions(dotInstructions);

        for (String foldInstruction : foldInstructions) {
            dots = executeFoldInstruction(foldInstruction, dots);
        }

        int maxY = Integer.MIN_VALUE;
        int maxX = Integer.MIN_VALUE;

        // Inefficient search because I can't know the exact dimensions otherwise :<
        for (Position dot : dots) {
            if (dot.x() > maxX) {
                maxX = dot.x();
            }
            if (dot.y() > maxY) {
                maxY = dot.y();
            }
        }

        final StringBuilder output = new StringBuilder();

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                if (dots.contains(new Position(y, x))) {
                    output.append('#');
                }
                else {
                    output.append('.');
                }
            }
            output.append("\r\n");
        }

        return output.toString();
    }

    private Set<Position> parseDotInstructions(String[] dotInstructions) {
        final Set<Position> dots = new HashSet<>();
        String[] coordinates;

        for (String dot : dotInstructions) {
            coordinates = dot.split(",");
            dots.add(new Position(Ut.cachedParseInt(coordinates[1]), Ut.cachedParseInt(coordinates[0])));
        }

        return dots;
    }

    private Set<Position> executeFoldInstruction(String foldInstruction, Set<Position> dots) {
        final Set<Position> foldedDots = new HashSet<>();

        final String[] tokens = foldInstruction.split("[ =]");
        final String direction = tokens[2];
        final int axis = Ut.cachedParseInt(tokens[3]);

        final UnaryOperator<Position> folder;

        if (direction.equals("y")) {
            folder = (position) -> {
                if (position.y() > axis) {
                    return new Position(axis * 2 - position.y(), position.x());
                }
                else {
                    return position;
                }
            };
        }
        else if (direction.equals("x")) {
            folder = (position) -> {
                if (position.x() > axis) {
                    return new Position(position.y(), axis * 2 - position.x());
                }
                else {
                    return position;
                }
            };
        }
        else {
            throw new IllegalStateException("Token should only be x or y, something went wrong!");
        }

        for (Position dot : dots) {
            foldedDots.add(folder.apply(dot));
        }

        return foldedDots;
    }
}
