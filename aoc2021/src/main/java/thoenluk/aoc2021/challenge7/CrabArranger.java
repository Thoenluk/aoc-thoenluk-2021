package thoenluk.aoc2021.challenge7;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Ut;

import java.util.Comparator;
import java.util.List;

public class CrabArranger implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        List<Integer> crabs = Ut.commaSeparatedStringToIntegerList(input);
        crabs.sort(null);

        int optimalPosition;

        if (crabs.size() % 2 == 1) {
            optimalPosition = crabs.get(crabs.size() / 2);
        }
        else {
            optimalPosition = (crabs.get((crabs.size() / 2) - 1) + crabs.get(crabs.size() / 2)) / 2;
        }

        int fuelSpent = 0;

        for (Integer crab : crabs) {
            fuelSpent += Math.abs(optimalPosition - crab);
        }

        // I'll be honest, I was expecting a little more than finding a median.

        return Integer.toString(fuelSpent);
    }

    @Override
    public String saveChristmasAgain(String input) {
        List<Integer> crabs = Ut.commaSeparatedStringToIntegerList(input);
        int furthestCrabPosition = crabs.stream().max(Comparator.naturalOrder()).orElseThrow();

        int optimalFuelCost = Integer.MAX_VALUE;
        int currentFuelCost;
        int distance;

        positionSearch:
        for (int position = 0; position <= furthestCrabPosition; position++) {
            currentFuelCost = 0;

            for (Integer crab : crabs) {
                distance = Math.abs(position - crab);
                currentFuelCost += (distance * (distance + 1)) / 2;
                if (currentFuelCost > optimalFuelCost) {
                    continue positionSearch;
                }
            }

            optimalFuelCost = currentFuelCost;
        }

        return Integer.toString(optimalFuelCost);
    }
}
