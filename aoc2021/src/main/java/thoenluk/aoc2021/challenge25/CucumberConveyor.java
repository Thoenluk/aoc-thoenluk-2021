package thoenluk.aoc2021.challenge25;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.UtMath;
import thoenluk.aoc2021.ut.UtParsing;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.HashMap;
import java.util.Map;

public class CucumberConveyor implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        Map<Position, Character> seafloor = new HashMap<>();
        final int maxY = lines.length;
        final int maxX = lines[0].length();

        for (int y = 0; y < maxY; y++) {
            String line = lines[y];
            for (int x = 0; x < maxX; x++) {
                seafloor.put(new Position(y, x), line.charAt(x));
            }
        }

        int movements = 1;
        int steps = 0;
        Map<Position, Character> nextStep;

        while (movements > 0) {
            movements = 0;
            steps++;

            nextStep = new HashMap<>(seafloor);

            for (Position location : seafloor.keySet()) {
                if (seafloor.get(location) == '>') {
                    final Position nextLocation = getNextLocation(location, seafloor, maxX, maxY);
                    if (seafloor.get(nextLocation) == '.') {
                        nextStep.put(nextLocation, '>');
                        nextStep.put(location, '.');
                        movements++;
                    }
                }
            }

            seafloor = nextStep;
            nextStep = new HashMap<>(seafloor);

            for (Position location : seafloor.keySet()) {
                if (seafloor.get(location) == 'v') {
                    final Position nextLocation = getNextLocation(location, seafloor, maxX, maxY);
                    if (seafloor.get(nextLocation) == '.') {
                        nextStep.put(nextLocation, 'v');
                        nextStep.put(location, '.');
                        movements++;
                    }
                }
            }
            seafloor = nextStep;
        }

        return Integer.toString(steps);
    }

    @Override
    public String saveChristmasAgain(String input) {
        return null;
    }

    private Position getNextLocation(Position location, Map<Position, Character> seafloor, int maxX, int maxY) {
        int targetY = location.y();
        int targetX = location.x();

        if (seafloor.get(location) == '>') {
            targetX += 1;
            targetX %= maxX;
        }
        else {
            targetY += 1;
            targetY %= maxY;
        }

        return new Position(targetY, targetX);
    }
}
