package thoenluk.aoc2021.challenge9;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.UtParsing;

import java.util.*;

import static thoenluk.aoc2021.ut.Position.NeighbourDirection.CARDINAL;

public class LowPointFinder implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final Map<Position, Integer> heightMap = UtParsing.multilineStringToPositionIntegerMap(input);
        final Set<Position> lowPoints = findLowPoints(heightMap);

        int riskLevelSum = 0;

        for (Position lowPoint : lowPoints) {
            riskLevelSum += heightMap.get(lowPoint) + 1;
        }

        return Integer.toString(riskLevelSum);
    }

    @Override
    public String saveChristmasAgain(String input) {
        Map<Position, Integer> heightMap = UtParsing.multilineStringToPositionIntegerMap(input);
        final Set<Position> lowPoints = findLowPoints(heightMap);

        List<Integer> threeLargestBasinSizes = new ArrayList<>(Arrays.asList(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE));
        final Set<Position> basin = new HashSet<>();
        final List<Position> unscannedNeighbours = new LinkedList<>();

        for (Position lowPoint : lowPoints) {
            basin.clear();
            basin.add(lowPoint);

            unscannedNeighbours.clear();
            unscannedNeighbours.add(lowPoint);

            while (!unscannedNeighbours.isEmpty()) {
                Position scanning = unscannedNeighbours.remove(0);
                for (Position neighbour : scanning.getNeighbours(CARDINAL)) {
                    if (heightMap.getOrDefault(neighbour, 9) != 9 && !basin.contains(neighbour)) {
                        basin.add(neighbour);
                        unscannedNeighbours.add(neighbour);
                    }
                }
            }

            final int basinSize = basin.size();
            if (threeLargestBasinSizes.get(0) < basinSize) {
                threeLargestBasinSizes.remove(0);
                threeLargestBasinSizes.add(basinSize);
                threeLargestBasinSizes.sort(null);
            }
        }

        int product = 1;

        for (Integer size : threeLargestBasinSizes) {
            product *= size;
        }

        return Integer.toString(product);
    }

    private Set<Position> findLowPoints(Map<Position, Integer> heightMap) {
        int height;
        int neighbourHeight;

        final Set<Position> lowPoints = new HashSet<>();

        lowPointSearch:
        for (Position position: heightMap.keySet()) {
            height = heightMap.get(position);

            for (Position neighbour : position.getNeighbours(CARDINAL)) {
                neighbourHeight = heightMap.getOrDefault(neighbour, Integer.MAX_VALUE);
                if (neighbourHeight <= height) {
                    continue lowPointSearch;
                }
            }

            lowPoints.add(position);
        }

        return lowPoints;
    }
}
