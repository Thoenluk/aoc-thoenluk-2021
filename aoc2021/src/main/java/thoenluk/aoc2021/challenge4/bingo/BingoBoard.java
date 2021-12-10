package thoenluk.aoc2021.challenge4.bingo;

import thoenluk.aoc2021.ut.Ut;
import thoenluk.aoc2021.ut.Position;

import java.util.HashMap;
import java.util.Map;

public class BingoBoard {
    private final Map<Integer, Position> board;
    private final int[] markedNumbersInRow;
    private final int[] markedNumbersInColumn;

    public BingoBoard(String[] boardRepresentation) {
        this.board = new HashMap<>();
        this.markedNumbersInRow = new int[boardRepresentation.length];
        this.markedNumbersInColumn = new int[boardRepresentation[0].trim().split(Ut.WHITE_SPACE_REGEX).length];

        int y, x;
        String[] numbersInRow;
        for (y = 0; y < boardRepresentation.length; y++) {
            numbersInRow = boardRepresentation[y].trim().split(Ut.WHITE_SPACE_REGEX);
            for (x = 0; x < numbersInRow.length; x++) {
                board.put(Ut.cachedParseInt(numbersInRow[x]), new Position(y, x));
            }
        }
    }

    public void markNumber(int number) {
        if (board.containsKey(number)) {
            Position position = board.remove(number);
            markedNumbersInRow[position.y()]++;
            markedNumbersInColumn[position.x()]++;
        }
    }

    public boolean isWin() {
        for (int marks : markedNumbersInRow) {
            if (marks == markedNumbersInRow.length) {
                return true;
            }
        }

        for (int marks : markedNumbersInColumn) {
            if (marks == markedNumbersInColumn.length) {
                return true;
            }
        }

        return false;
    }

    public int getScore() {
        int score = 0;
        for (Integer unmarkedNumber : board.keySet()) {
            score += unmarkedNumber;
        }
        return score;
    }
}
