package thoenluk.aoc2021.challenge11;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.UtParsing;

import java.util.*;

import static thoenluk.aoc2021.ut.Position.NeighbourDirection.OMNIDIRECTIONAL;

public class OctopusChargingStation implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final Map<Position, Integer> octopuses = UtParsing.multilineStringToPositionIntegerMap(input);
        final List<Position> octopodesReadyToFlash = new LinkedList<>();
        int totalFlashes = 0;

        for (int i = 0; i < 100; i++) {
            chargeOctoghettiNaturally(octopuses, octopodesReadyToFlash);

            totalFlashes += processCrossFlashtamination(octopuses, octopodesReadyToFlash);
        }

        return Integer.toString(totalFlashes);
    }

    private void chargeOctoghettiNaturally(Map<Position, Integer> octopuses, List<Position> octopodesReadyToFlash) {
        octopuses.replaceAll((position, charge) -> {
            charge++;
            if (charge > 9) {
                octopodesReadyToFlash.add(position);
            }
            return charge;
        });
    }

    private int processCrossFlashtamination(Map<Position, Integer> octopuses, List<Position> octopodesReadyToFlash) {
        final Set<Position> octopiThatFlashed = new HashSet<>();
        Position octopus;
        int neighbourCharge;

        while (!octopodesReadyToFlash.isEmpty()) {
            octopus = octopodesReadyToFlash.remove(0);
            octopuses.put(octopus, 0);
            octopiThatFlashed.add(octopus);

            for (Position neighbour : octopus.getNeighbours(OMNIDIRECTIONAL)) {
                if (octopiThatFlashed.contains(neighbour) || !octopuses.containsKey(neighbour)) {
                    continue;
                }
                neighbourCharge = octopuses.get(neighbour);
                neighbourCharge++;
                octopuses.put(neighbour, neighbourCharge);
                if (neighbourCharge > 9) {
                    if(!octopodesReadyToFlash.contains(neighbour)) {
                        octopodesReadyToFlash.add(neighbour);
                    }
                }
            }
        }

        return octopiThatFlashed.size();
    }

    @Override
    public String saveChristmasAgain(String input) {
        final Map<Position, Integer> octopuses = UtParsing.multilineStringToPositionIntegerMap(input);
        final List<Position> octopodesReadyToFlash = new LinkedList<>();

        for (int i = 1; true; i++) {
            chargeOctoghettiNaturally(octopuses, octopodesReadyToFlash);

            if (processCrossFlashtamination(octopuses, octopodesReadyToFlash) == octopuses.size()) {
                return Integer.toString(i);
            }
        }
    }
}
