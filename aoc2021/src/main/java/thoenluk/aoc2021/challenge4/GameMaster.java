package thoenluk.aoc2021.challenge4;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.Ut;
import thoenluk.aoc2021.challenge4.bingo.BingoBoard;

import java.util.*;

public class GameMaster implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        List<String> lines = new LinkedList<>(Arrays.asList(Ut.splitMultilineString(input)));
        List<Integer> drawnNumbers = parseDrawnNumbers(lines.remove(0));
        lines.remove(0);

        Set<BingoBoard> boards = setupBoards(lines);

        for (int drawnNumber : drawnNumbers) {
            for (BingoBoard board : boards) {
                board.markNumber(drawnNumber);
                if (board.isWin()) {
                    return Integer.toString(drawnNumber * board.getScore());
                }
            }
        }

        return null;
    }

    @Override
    public String saveChristmasAgain(String input) {
        List<String> lines = new LinkedList<>(Arrays.asList(Ut.splitMultilineString(input)));
        List<Integer> drawnNumbers = parseDrawnNumbers(lines.remove(0));
        lines.remove(0);

        Set<BingoBoard> boards = setupBoards(lines);
        Set<BingoBoard> boardsThatWon = new HashSet<>();

        for (int drawnNumber : drawnNumbers) {
            for (BingoBoard board : boards) {
                board.markNumber(drawnNumber);
                if (board.isWin()) {
                    if (boards.size() == 1) {
                        return Integer.toString(drawnNumber * board.getScore());
                    }
                    boardsThatWon.add(board);
                }
            }
            boards.removeAll(boardsThatWon); // I like this line. It amuses me.
            boardsThatWon.clear();
        }

        return null;
    }

    private List<Integer> parseDrawnNumbers(String drawnNumbersLine) {
        List<Integer> drawnNumbers = new LinkedList<>();
        String[] drawnNumberTokens = drawnNumbersLine.split(",");
        for (String token : drawnNumberTokens) {
            drawnNumbers.add(Ut.cachedParseInt(token));
        }
        return drawnNumbers;
    }

    private Set<BingoBoard> setupBoards(List<String> inputLines) {
        HashSet<BingoBoard> boards = new HashSet<>();
        List<String> currentBoard = new LinkedList<>();

        for (String line : inputLines) {
            if (line.isBlank()) {
                boards.add(new BingoBoard(currentBoard.toArray(new String[0])));
                currentBoard.clear();
            }
            else {
                currentBoard.add(line);
            }
        }

        if (!currentBoard.isEmpty()) {
            boards.add(new BingoBoard(currentBoard.toArray(new String[0])));
        }
        return boards;
    }
}
