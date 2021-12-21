package thoenluk.aoc2021.challenge19.scanner;

import thoenluk.aoc2021.ut.ThreeDPosition;
import thoenluk.aoc2021.ut.UtParsing;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Scanner {

    //---- Fields

    private final Set<Beacon> beacons = new HashSet<>();
    private final Map<Beacon, Beacon> matchingBeacons = new HashMap<>();
    private ThreeDPosition position = null;


    //---- Constructor

    public Scanner(String data) {
        final String[] lines = UtStrings.splitMultilineString(data);
        List<Integer> coordinates;

        for (int i = 1; i < lines.length; i++) {
            coordinates = UtParsing.commaSeparatedStringToIntegerList(lines[i]);
            if (coordinates.size() != 3) throw new AssertionError();
            beacons.add(new Beacon(
                            new ThreeDPosition(
                                    coordinates.get(0),
                                    coordinates.get(1),
                                    coordinates.get(2)
                            )
                        )
            );
        }

        for (Beacon beacon : beacons) {
            for (Beacon other : beacons) {
                beacon.addDistanceFromOtherBeacon(other);
            }

            Collections.sort(beacon.distances());
        }
    }

    public boolean locateOtherScanner(Scanner scanner) {
        // 1. For each beacon, establish if the other scanner sees it too by comparing the distance sets:
        // A connection exists iff there are 12 beacons in range of both scanners. Ergo, each beacon must have at least
        // 11 visible neighbours with distances d1-d11. The same beacon seen by two scanners must have 11 or more
        // distances in its list of distances as seen by each scanner.
        List<Beacon> commonBeacons = getCommonBeacons(scanner);

        if (commonBeacons.size() < 12) {
            return false;
        }

        final Beacon commonBeaconA = commonBeacons.get(0);

        // 2. A common beacon A is found. Locate a second beacon B they can both see such that (A -> B) has three
        // coordinates of distinct magnitude, i.e. abs(xA - xB) != abs(yA - yB) and so on for z.
        // Do this using only this scanner's view as it is already canonical.
        final Beacon commonBeaconB = getBeaconWithDistinctMagnitudeDistanceVector(commonBeaconA, commonBeacons);


        // 3. Order the other scanner to reorient itself such that (A -> B) for it is equal to (A -> B) for this scanner.
        scanner.reorientToMatchVectorBetweenBeacons(
                commonBeaconA,
                commonBeaconB,
                commonBeaconA.threeDPosition()
                                .subtract(
                                        commonBeaconB.threeDPosition()
                                )
        );


        // 4. The other scanner is now in canonical orientation. Add the offsets S1 -> A and A -> S2 to determine
        // (S1 -> S2), and then tell S2 that its threeDPosition is S1 + (S1 -> S2)
        final Beacon commonBeaconAFromOtherScanner = scanner.getMatchingBeacon(commonBeaconA);
        final ThreeDPosition commonBeaconAToOtherScanner = commonBeaconAFromOtherScanner.threeDPosition().negate();

        scanner.setPosition(commonBeaconA.threeDPosition().add(commonBeaconAToOtherScanner));

        return true;
    }

    public boolean canSeeBeacon(Beacon beacon) {
        return getMatchingBeacon(beacon) != null;
    }

    public void reorientToMatchVectorBetweenBeacons(Beacon firstBeacon, Beacon secondBeacon, ThreeDPosition canonicalVector) {
        final Beacon firstMatchingBeacon = getMatchingBeacon(firstBeacon);
        final Beacon secondMatchingBeacon = getMatchingBeacon(secondBeacon);

        if (firstMatchingBeacon == null) throw new AssertionError();
        if (secondMatchingBeacon == null) throw new AssertionError();

        ThreeDPosition vector = firstMatchingBeacon.threeDPosition().subtract(secondMatchingBeacon.threeDPosition());

        UnaryOperator<ThreeDPosition> rearranger = vector.getRearrangerToMatch(canonicalVector);

        vector = rearranger.apply(vector);

        UnaryOperator<ThreeDPosition> signer = vector.getSignerToMatch(canonicalVector);

        if (!signer.apply(vector).equals(canonicalVector)) throw new AssertionError();

        for (Beacon beacon : beacons) {
            beacon.setThreeDPosition(
                    signer.apply(
                            rearranger.apply(
                                    beacon.threeDPosition()
                            )
                    )
            );
        }
    }

    public void setPosition(ThreeDPosition position) {
        this.position = position;

        for (Beacon beacon : beacons) {
            beacon.setThreeDPosition(position.add(beacon.threeDPosition()));
        }
    }

    public Set<ThreeDPosition> getBeaconLocations() {
        return beacons.stream()
                .map(Beacon::threeDPosition)
                .collect(Collectors.toSet());
    }

    private List<Beacon> getCommonBeacons(Scanner scanner) {
        List<Beacon> commonBeacons = new ArrayList<>();

        for (Beacon beacon : beacons) {
            if (scanner.canSeeBeacon(beacon)) {
                commonBeacons.add(beacon);
            }
        }

        return commonBeacons;
    }

    private Beacon getMatchingBeacon(Beacon beacon) {
        if (!matchingBeacons.containsKey(beacon)) {
            Beacon matchingBeacon = null;
            for (Beacon beaconThisCanSee : beacons) {
                if (beacon.probablyEquals(beaconThisCanSee)) {
                    matchingBeacon = beaconThisCanSee;
                    break;
                }
            }

            matchingBeacons.put(beacon, matchingBeacon);
        }

        return matchingBeacons.get(beacon);
    }

    private Beacon getBeaconWithDistinctMagnitudeDistanceVector(Beacon from, List<Beacon> commonBeacons) {
        final ThreeDPosition fromPosition = from.threeDPosition();

        for (Beacon other : commonBeacons) {
            final ThreeDPosition distanceVector = fromPosition.subtract(other.threeDPosition());

            if (distanceVector.hasDistinctMagnitudeCoordinates()) {
                return other;
            }
        }

        throw new IllegalStateException("Somehow there is not a single visible beacon with distinct distance vector?");
    }

    public ThreeDPosition getPosition() {
        return position;
    }
}
