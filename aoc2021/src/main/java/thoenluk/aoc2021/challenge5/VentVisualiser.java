package thoenluk.aoc2021.challenge5;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge5.ventline.VentLine;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.Ut;

import java.util.HashMap;
import java.util.Map;

public class VentVisualiser implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        Map<Position, Integer> ventsInLocation = new HashMap<>();
        String[] lines = Ut.splitMultilineString(input);
        VentLine ventLine;

        for (String line : lines) {
            ventLine = new VentLine(line);
            if (ventLine.isHorizontalOrVertical()) {
                for (Position position : ventLine.getPositionsOfPointsInLine()) {
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
        Map<Position, Integer> ventsInLocation = new HashMap<>();
        String[] lines = Ut.splitMultilineString(input);
        VentLine ventLine;

        for (String line : lines) {
            ventLine = new VentLine(line);
            for (Position position : ventLine.getPositionsOfPointsInLine()) {
                ventsInLocation.compute(position, (key, value) -> (value == null) ? 1 : value + 1);
            }
        }

        return Long.toString(ventsInLocation.values()
                .stream()
                .filter((count) -> count > 1)
                .count());
    }

}
