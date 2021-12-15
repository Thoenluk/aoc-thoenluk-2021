package thoenluk.aoc2021.challenge15;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;

import static thoenluk.aoc2021.ut.Position.NeighbourDirection.CARDINAL;

public class ChitonNavigator implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final Map<Position, Integer> individualRiskLevels = Ut.multilineStringToPositionIntegerMap(input);
        final Map<Position, Integer> lowestTotalRisks = computeLowestTotalRisks(individualRiskLevels);

        return Integer.toString(getRiskLevelOfBottomRightNode(lowestTotalRisks));
    }

    @Override
    public String saveChristmasAgain(String input) {
        final Map<Position, Integer> individualRiskLevels = computeIndividualRiskLevelsEnlarged(input);
        final Map<Position, Integer> lowestTotalRisks = computeLowestTotalRisks(individualRiskLevels);

        return Integer.toString(getRiskLevelOfBottomRightNode(lowestTotalRisks));
    }

    private Map<Position, Integer> computeLowestTotalRisks(Map<Position, Integer> individualRiskLevels) {
        final Map<Position, Integer> lowestTotalRisks = new HashMap<>();
        lowestTotalRisks.put(new Position(0, 0), 0);

        final PriorityQueue<Position> nodesToExplore = new PriorityQueue<>(Comparator.comparingInt(lowestTotalRisks::get));
        nodesToExplore.add(new Position(0, 0));

        Position node;
        int nodeRisk;

        while (!nodesToExplore.isEmpty()) {
            node = nodesToExplore.remove();
            nodeRisk = lowestTotalRisks.get(node);

            for (Position neighbour : node.getNeighbours(CARDINAL)) {
                if (individualRiskLevels.containsKey(neighbour)) {
                    final int riskLevelFromNode = nodeRisk + individualRiskLevels.get(neighbour);
                    if (!lowestTotalRisks.containsKey(neighbour)) {
                        lowestTotalRisks.put(neighbour, riskLevelFromNode);
                        nodesToExplore.add(neighbour);
                    }
                }
            }
        }

        return lowestTotalRisks;
    }

    private int getRiskLevelOfBottomRightNode(Map<Position, Integer> lowestTotalRisks) {
        Position bottomRight = new Position(-1, -1);

        for (Position position : lowestTotalRisks.keySet()) {
            if (position.y() + position.x() > bottomRight.y() + bottomRight.x()) {
                bottomRight = position;
            }
        }

        return lowestTotalRisks.get(bottomRight);
    }

    private Map<Position, Integer> computeIndividualRiskLevelsEnlarged(String input) {
        final Map<Position, Integer> individualRiskLevelsTemplate = Ut.multilineStringToPositionIntegerMap(input);
        final String[] lines = Ut.splitMultilineString(input);

        final Map<Position, Integer> individualRiskLevels = new HashMap<>();

        final int xSize = lines[0].length();
        final int ySize = lines.length;
        Position position;
        int templateRiskLevel, riskLevel;

        for (Position templatePosition : individualRiskLevelsTemplate.keySet()) {
            templateRiskLevel = individualRiskLevelsTemplate.get(templatePosition);
            for (int xRepetitions = 0; xRepetitions < 5; xRepetitions++) {
                for (int yRepetitions = 0; yRepetitions < 5; yRepetitions++) {
                    position = new Position(templatePosition.y() + yRepetitions * ySize, templatePosition.x() + xRepetitions * xSize);
                    riskLevel = templateRiskLevel + xRepetitions + yRepetitions;
                    riskLevel -= 1;
                    riskLevel %= 9;
                    riskLevel += 1;

                    individualRiskLevels.put(position, riskLevel);
                }
            }
        }

        return individualRiskLevels;
    }
}
