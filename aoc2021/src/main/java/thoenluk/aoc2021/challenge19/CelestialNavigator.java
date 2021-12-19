package thoenluk.aoc2021.challenge19;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge19.scanner.Scanner;
import thoenluk.aoc2021.ut.ThreeDPosition;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;

public class CelestialNavigator implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        List<Scanner> canonicalScanners = parseAndCanonisiseScanners(input);

        Set<ThreeDPosition> beacons = new HashSet<>();

        for (Scanner canonicalScanner : canonicalScanners) {
            beacons.addAll(canonicalScanner.getBeaconLocations());
        }

        return Integer.toString(beacons.size());
    }

    @Override
    public String saveChristmasAgain(String input) {
        List<Scanner> canonicalScanners = parseAndCanonisiseScanners(input);

        int largestDistance = Integer.MIN_VALUE;

        for (Scanner canonicalScanner : canonicalScanners) {
            for (Scanner otherScanner : canonicalScanners) {
                if (otherScanner == canonicalScanner) {
                    continue;
                }

                final int distance = canonicalScanner.getPosition().getDistanceFrom(otherScanner.getPosition());

                if (largestDistance < distance) {
                    largestDistance = distance;
                }
            }
        }

        return Integer.toString(largestDistance);
    }

    private List<Scanner> parseAndCanonisiseScanners(String input) {
        String[] scannerDatas = Ut.splitStringWithEmptyLines(input);
        List<Scanner> discordantScanners = new ArrayList<>();

        for (String scannerData : scannerDatas) {
            discordantScanners.add(new Scanner(scannerData));
        }

        discordantScanners.get(0).setPosition(new ThreeDPosition(0, 0, 0));

        List<Scanner> canonicalScanners = new ArrayList<>();
        canonicalScanners.add(discordantScanners.remove(0));

        for (int i = 0; i < canonicalScanners.size(); i++) {
            final Scanner canonicalScanner = canonicalScanners.get(i);

            final Iterator<Scanner> it = discordantScanners.listIterator();

            while (it.hasNext()) {
                final Scanner discordantScanner = it.next();
                if (canonicalScanner.locateOtherScanner(discordantScanner)) {
                    it.remove();
                    canonicalScanners.add(discordantScanner);
                }
            }
        }

        if (!discordantScanners.isEmpty()) throw new AssertionError();

        return canonicalScanners;
    }
}
