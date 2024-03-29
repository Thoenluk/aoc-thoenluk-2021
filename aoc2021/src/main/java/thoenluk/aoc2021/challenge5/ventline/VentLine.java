package thoenluk.aoc2021.challenge5.ventline;

import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.UtParsing;

import java.util.LinkedList;
import java.util.List;

public class VentLine {
        final int startingY, startingX, endingY, endingX;

        public VentLine(String inputLine) {
            String[] coordinates = inputLine.split(",| -> ");
            this.startingX = UtParsing.cachedParseInt(coordinates[0]);
            this.startingY = UtParsing.cachedParseInt(coordinates[1]);
            this.endingX = UtParsing.cachedParseInt(coordinates[2]);
            this.endingY = UtParsing.cachedParseInt(coordinates[3]);
        }

        public boolean isHorizontalOrVertical() {
            return startingX == endingX || startingY == endingY;
        }

        public List<Position> getPositionsOfPointsInLine() {
            final int normalisedXDirection = Math.round(Math.signum(endingX - startingX));
            final int normalisedYDirection = Math.round(Math.signum(endingY - startingY));
            List<Position> positions = new LinkedList<>();
            int y = startingY, x = startingX;

            positions.add(new Position(y, x));

            while (!(y == endingY && x == endingX)) {
                y += normalisedYDirection;
                x += normalisedXDirection;
                positions.add(new Position(y, x));
            }

            return positions;
        }
}
