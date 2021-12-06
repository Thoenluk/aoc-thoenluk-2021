package thoenluk.aoc2021.challenge5;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Ut;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VentVisualiser implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        Map<Ut.Position, Integer> ventsInLocation = new HashMap<>();
        String[] lines = Ut.splitMultilineString(input);
        VentLine ventLine;

        for (String line : lines) {
            ventLine = new VentLine(line);
            if (ventLine.isHorizontalOrVertical()) {
                for (Ut.Position position : ventLine.getPositionsOfPointsInLine()) {
                    ventsInLocation.compute(position, (key, value) -> (value == null) ? 1 : value + 1);
                }
            }
        }

        return Long.toString(ventsInLocation.values()
                .stream()
                .filter((count) -> count > 1)
                .count());
    }

    @Override
    public String saveChristmasAgain(String input) {
        Map<Ut.Position, Integer> ventsInLocation = new HashMap<>();
        String[] lines = Ut.splitMultilineString(input);
        VentLine ventLine;

        for (String line : lines) {
            ventLine = new VentLine(line);
            for (Ut.Position position : ventLine.getPositionsOfPointsInLine()) {
                ventsInLocation.compute(position, (key, value) -> (value == null) ? 1 : value + 1);
            }
        }

        return Long.toString(ventsInLocation.values()
                .stream()
                .filter((count) -> count > 1)
                .count());
    }

    private static class VentLine {
        final int startingY, startingX, endingY, endingX;

        private VentLine(String inputLine) {
            String[] coordinates = inputLine.split(",| -> ");
            this.startingX = Ut.cachedParseInt(coordinates[0]);
            this.startingY = Ut.cachedParseInt(coordinates[1]);
            this.endingX = Ut.cachedParseInt(coordinates[2]);
            this.endingY = Ut.cachedParseInt(coordinates[3]);
        }

        public boolean isHorizontalOrVertical() {
            return startingX == endingX || startingY == endingY;
        }

        public List<Ut.Position> getPositionsOfPointsInLine() {
            final int normalisedXDirection = Math.round(Math.signum(endingX - startingX));
            final int normalisedYDirection = Math.round(Math.signum(endingY - startingY));
            List<Ut.Position> positions = new LinkedList<>();
            int y = startingY, x = startingX;

            positions.add(new Ut.Position(y, x));

            while (!(y == endingY && x == endingX)) {
                y += normalisedYDirection;
                x += normalisedXDirection;
                positions.add(new Ut.Position(y, x));
            }

            return positions;
        }
    }
}
