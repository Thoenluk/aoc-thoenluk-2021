package thoenluk.aoc2021.challenge2;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.UtParsing;
import thoenluk.aoc2021.ut.UtStrings;

public class PilotingModule implements ChristmasSaver {

    @Override
    public String saveChristmas(String input) {
        final String[] instructions = UtStrings.splitMultilineString(input);
        int depth = 0, horizontalPosition = 0;

        String[] splitInstruction;
        String direction;
        int magnitude;

        for (String instruction : instructions) {
            splitInstruction = instruction.split(UtStrings.WHITE_SPACE_REGEX);
            direction = splitInstruction[0];
            magnitude = UtParsing.cachedParseInt(splitInstruction[1]);

            switch (direction) {
                case "forward" -> horizontalPosition += magnitude;
                case "down" -> depth += magnitude;
                case "up" -> depth -= magnitude;
            }
        }

        return Integer.toString(depth * horizontalPosition);
    }

    @Override
    public String saveChristmasAgain(String input) {
        final String[] instructions = UtStrings.splitMultilineString(input);
        int depth = 0, horizontalPosition = 0, aim = 0;

        String[] splitInstruction;
        String direction;
        int magnitude;

        for (String instruction : instructions) {
            splitInstruction = instruction.split(UtStrings.WHITE_SPACE_REGEX);
            direction = splitInstruction[0];
            magnitude = UtParsing.cachedParseInt(splitInstruction[1]);

            switch (direction) {
                case "forward" -> {
                    horizontalPosition += magnitude;
                    depth += magnitude * aim;
                }
                case "down" -> aim += magnitude;
                case "up" -> aim -= magnitude;
            }
        }

        return Integer.toString(depth * horizontalPosition);
    }
}
