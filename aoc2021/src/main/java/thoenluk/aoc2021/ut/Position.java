package thoenluk.aoc2021.ut;

import java.util.HashSet;
import java.util.Set;

public record Position(int y, int x) {

    public Set<Position> getNeighbours(NeighbourDirection neighbourDirection) {
        Set<Position> neighbours = new HashSet<>();
        for (Position direction : neighbourDirection.getDirections()) {
            neighbours.add(new Position(this.y + direction.y(), this.x + direction.x()));
        }
        return neighbours;
    }

    public enum NeighbourDirection {
        CARDINAL(Set.of(new Position(-1, 0), new Position(0, -1), new Position(0, 1), new Position(1, 0))),
        DIAGONAL(Set.of(new Position(-1, -1), new Position(-1, 1), new Position(1, -1), new Position(1, 1))),
        OMNIDIRECTIONAL(Set.of(new Position(-1, 0), new Position(0, -1), new Position(0, 1), new Position(1, 0),
                                new Position(-1, -1), new Position(-1, 1), new Position(1, -1), new Position(1, 1)));

        private final Set<Position> directions;

        /* private */ NeighbourDirection(Set<Position> directions) {
            this.directions = directions;
        }

        private static int[] dir(int y, int x) {
            return new int[]{y, x};
        }

        public Set<Position> getDirections() {
            return directions;
        }
    }
}