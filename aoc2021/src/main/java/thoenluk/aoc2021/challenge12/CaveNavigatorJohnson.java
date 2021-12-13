package thoenluk.aoc2021.challenge12;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge12.cave.Cave;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;

public class CaveNavigatorJohnson implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final Cave start = parseCaveNetworkAndReturnStart(input);
        final Stack<Cave> path = new Stack<>();
        int paths = 0;

        paths += getPathsFromCaveToEnd(start, path, false);

        return Integer.toString(paths);
    }

    @Override
    public String saveChristmasAgain(String input) {
        final Cave start = parseCaveNetworkAndReturnStart(input);
        final Stack<Cave> path = new Stack<>();
        int paths = 0;

        paths += getPathsFromCaveToEnd(start, path, true);

        return Integer.toString(paths);
    }

    private Cave parseCaveNetworkAndReturnStart(String input) {
        final String[] lines = Ut.splitMultilineString(input);
        Map<String, Cave> caveNetwork = new HashMap<>();

        for (String line : lines) {
            final String[] caves = line.split("-");
            final Cave startingCave = caveNetwork.computeIfAbsent(caves[0], Cave::new);
            final Cave endingCave = caveNetwork.computeIfAbsent(caves[1], Cave::new);

            startingCave.addConnection(endingCave);
            endingCave.addConnection(startingCave);
        }

        return caveNetwork.get("start");
    }

    private int getPathsFromCaveToEnd(Cave startingCave, Stack<Cave> existingPath, boolean mayRevisitSmallCave) {
        if (startingCave.isEnd()) {
            return 1;
        }

        int paths = 0;
        existingPath.push(startingCave);

        for (Cave connectedCave : startingCave.getConnectedCaves()) {
            if (!(existingPath.contains(connectedCave) && connectedCave.isSmall())) {
                paths += getPathsFromCaveToEnd(connectedCave, existingPath, mayRevisitSmallCave);
            }
            else if (mayRevisitSmallCave && !connectedCave.isStart()) {
                paths += getPathsFromCaveToEnd(connectedCave, existingPath, false);
            }
        }

        existingPath.pop();

        return paths;
    }
}
