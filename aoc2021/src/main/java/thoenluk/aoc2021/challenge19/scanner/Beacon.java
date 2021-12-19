package thoenluk.aoc2021.challenge19.scanner;

import thoenluk.aoc2021.ut.ThreeDPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Beacon {
    private ThreeDPosition threeDPosition;
    private final List<Integer> distances;

    public Beacon(ThreeDPosition threeDPosition, List<Integer> distances) {
        this.threeDPosition = threeDPosition;
        this.distances = distances;
    }

    public Beacon(ThreeDPosition position) {
        this(position, new ArrayList<>());
    }

    public void addDistanceFromOtherBeacon(Beacon other) {
        if (other != this) {
            distances.add(this.threeDPosition().getDistanceFrom(other.threeDPosition()));
        }
    }

    public boolean probablyEquals(Beacon other) {
        int sharedDistances = 0;

        List<Integer> beaconDistances = new ArrayList<>(this.distances());
        List<Integer> beaconThisCanSeeDistances = new ArrayList<>(other.distances());

        for (Integer distance : beaconDistances) {
            if (beaconThisCanSeeDistances.remove(distance)) {
                sharedDistances++;
            }
            if (sharedDistances == 11) {
                return true;
            }
        }

        return false;
    }

    public ThreeDPosition threeDPosition() {
        return threeDPosition;
    }

    public List<Integer> distances() {
        return distances;
    }

    public void setThreeDPosition(ThreeDPosition threeDPosition) {
        this.threeDPosition = threeDPosition;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Beacon) obj;
        return Objects.equals(this.threeDPosition, that.threeDPosition) &&
                Objects.equals(this.distances, that.distances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threeDPosition, distances);
    }

    @Override
    public String toString() {
        return "Beacon[" +
                "threeDPosition=" + threeDPosition + ", " +
                "distances=" + distances + ']';
    }

}
