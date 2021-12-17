package thoenluk.aoc2021.challenge17;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Area;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;

public class SuperCoolProbeLauncher69000TronDeluxe implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        Area target = parseAreaFromInput(input);

        int maximumHeight = 0;

        for (int startingYVelocity = 0; startingYVelocity <= -1 * target.bottomLeft().y(); startingYVelocity++) {
            if (!getTravelTimesAtWhichYVelocityHitsTarget(startingYVelocity + 1, target).isEmpty()) {
                maximumHeight = (startingYVelocity * (startingYVelocity + 1)) / 2;
            }
        }

        return Integer.toString(maximumHeight);
    }

    @Override
    public String saveChristmasAgain(String input) {
        Area target = parseAreaFromInput(input);

        Set<Integer> xVelocitiesThatHitTarget = new HashSet<>();

        for (int startingXVelocity = 0; startingXVelocity <= target.topRight().x(); startingXVelocity++) {
            if (xVelocityHitsTarget(startingXVelocity, target)) {
                xVelocitiesThatHitTarget.add(startingXVelocity);
            }
        }

        int distinctVelocities = 0;

        for (int yVelocity = target.bottomLeft().y(); yVelocity <= -1 * target.bottomLeft().y(); yVelocity++) {
            for (int xVelocity : xVelocitiesThatHitTarget) {
                if (trajectoryHitsTarget(yVelocity, xVelocity, target)) {
                    distinctVelocities++;
                }
            }
        }

        return Integer.toString(distinctVelocities);
    }

    // Don't let anyone touch your private Area!
    private Area parseAreaFromInput(String input) {
        String[] coordinates = input.replaceAll("[^-\\d]", " ")
                .trim()
                .split("\s+");
        Position topLeft = new Position(Ut.cachedParseInt(coordinates[2]), Ut.cachedParseInt(coordinates[0]));
        Position bottomRight = new Position(Ut.cachedParseInt(coordinates[3]), Ut.cachedParseInt(coordinates[1]));

        return new Area(topLeft, bottomRight);
    }

    private boolean xVelocityHitsTarget(int xVelocity, Area target) {
        int location = 0;

        while (xVelocity >= 0 && location <= target.topRight().x()) {
            location += xVelocity;
            xVelocity--;

            if (target.containsPosition(new Position(target.bottomLeft().y(), location))) {
                return true;
            }
        }

        return false;
    }

    private Set<Integer> getTravelTimesAtWhichYVelocityHitsTarget(int yVelocity, Area target) {
        yVelocity *= -1;
        int location = 0;
        int travelTime = 0;
        Set<Integer> travelTimes = new HashSet<>();

        while (location >= target.bottomLeft().y()) {
            location += yVelocity;
            yVelocity--;
            travelTime++;

            if (target.containsPosition(new Position(location, target.bottomLeft().x()))) {
                travelTimes.add(travelTime);
            }
        }

        return travelTimes;
    }

    private boolean trajectoryHitsTarget(int yVelocity, int xVelocity, Area target) {
        int xLocation = 0;
        int yLocation = 0;

        while (xLocation <= target.topRight().x() && yLocation >= target.bottomLeft().y()) {
            xLocation += xVelocity;
            yLocation += yVelocity;

            xVelocity = xVelocity == 0 ? 0 : xVelocity - 1;
            yVelocity--;

            if (target.containsPosition(new Position(yLocation, xLocation))) {
                return true;
            }
        }

        return false;
    }
}
